package com.pretius.jddl.action.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pretius.jddl.JddlCanNotDeserializeCollectionException;
import com.pretius.jddl.JddlCanNotDeserializeObjectException;
import com.pretius.jddl.JddlClassInstantiationException;
import com.pretius.jddl.JddlException;
import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationEvent;
import com.pretius.jddl.model.DeserializationResult;
import com.pretius.jddl.model.impl.DeserializationEventImpl;
import com.pretius.jddl.model.impl.DeserializationResultImpl;
import com.pretius.jddl.util.JddlReflectionUtils;

public class DeserializeArrayAction {

    // declared here to make the lambda's actual class visible in logs
    public static DeserializationAction getLambda() {
        return DeserializeArrayAction::deserialize;
    }
    
    public static DeserializationResult deserialize(DeserializationEvent event)
            throws InstantiationException, IllegalAccessException, IOException
    {
        JsonNode jsonNode = event.getJsonNode();
        if ( ! (jsonNode instanceof ArrayNode)) {
            throw new JddlException("encountered json node class=["+jsonNode.getClass()+"] where expected an ArrayNode; field=["+event.getFieldName()+"], field class=["+event.getClass()+"]");
        }
        
        Class<?> containerClass = determineContainerClass(event);
        
        Class<?> elementClass = determineElementClass(event);
        
        Object retObj = deserializeJsonArray((ArrayNode) jsonNode, containerClass, elementClass, event.getFieldName(), event.getObjectMapperSupplier(), event.getEventHandle());
        return new DeserializationResultImpl(event.getFieldName(), retObj);
    }

    private static Class<?> determineContainerClass(DeserializationEvent event) {
        if (event.getContainerType() != null) {
            return (Class<?>) event.getContainerType();
        } else if (event.getExpectedType() instanceof ParameterizedType) {
            Type containerType = ((ParameterizedType) event.getExpectedType()).getRawType();
            if (containerType instanceof Class) {
                Class<?> containerClass = (Class<?>) containerType;
                if (containerClass.isInterface()) {
                    if (List.class.isAssignableFrom(containerClass)) {
                        return ArrayList.class;
                    } else if (Set.class.isAssignableFrom(containerClass)) {
                        return HashSet.class;
                    } else if (Map.class.isAssignableFrom(containerClass)) {
                        return HashMap.class;
                    } else if (Collection.class.isAssignableFrom(containerClass)) {
                        return ArrayList.class;
                    }
                } else {
                    return containerClass;
                }
            } 
            throw new JddlCanNotDeserializeCollectionException("Can not determine container class for type=["+containerType+"]");
        } else if (event.getExpectedType() instanceof Class && ((Class<?>)event.getExpectedType()).isArray()) {
            return (Class<?>) event.getExpectedType();
        } else {
            throw new JddlCanNotDeserializeCollectionException("Can not determine instance type for expectedType=["+event.getExpectedType()+"] containerType=["+event.getContainerType()+"]");
        }
    }

    static Class<?> determineElementClass(DeserializationEvent event) {
        Type expectedType = event.getExpectedType();
        
        if (expectedType instanceof Class) {
            Class<?> expectedClass = (Class<?>) expectedType;
            if (expectedClass.isArray()) {
                return expectedClass.getComponentType();
            } else {
                return expectedClass;
            }
        }
        if (! (expectedType instanceof ParameterizedType)) {
            throw new JddlCanNotDeserializeCollectionException("Can not determine element type for container type=["+expectedType+"];"
                    + " no parameterization/ParameterizedType found");
        }
        ParameterizedType parameterizedType = (ParameterizedType) expectedType;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments != null && actualTypeArguments.length == 1) {
            Type elementType = actualTypeArguments[0];
            if (elementType instanceof Class) {
                Class<?> elementClass = (Class<?>) elementType;
                return elementClass;
            }
        }

        throw new JddlCanNotDeserializeCollectionException("Can not determine element type for container type=["+expectedType+"];"
                + " use a named rule for this field or specify concrete class type in collection instead of a wildcard or an interface");
    }

    static Object deserializeJsonArray(ArrayNode arrayNode, Class<?> containerClass, Class<?> elementClass,
            String fieldName, Supplier<ObjectMapper> objectMapperSupplier, DeserializationAction defaultEventHandler)
    {
        try {
            if (containerClass.isArray()) {
                return deserializeIntoArray(arrayNode, elementClass, fieldName, objectMapperSupplier, defaultEventHandler);
            } else if (Collection.class.isAssignableFrom(containerClass)) {
                return deserializeIntoCollection(arrayNode, containerClass, elementClass, fieldName, objectMapperSupplier, defaultEventHandler);
            }
        } catch (JddlCanNotDeserializeCollectionException | JddlCanNotDeserializeObjectException | JddlClassInstantiationException e) {
            throw new JddlCanNotDeserializeCollectionException("Error deserializing array for collection element in field=["+fieldName+"] containerType=["+containerClass+"]", e);
        }
        
        throw new JddlException(
                String.format("%s: can't create object of class %s from JSON array", fieldName, containerClass));
    }

    static Object deserializeIntoArray(ArrayNode arrayNode, Class<?> elementClass, String fieldName, Supplier<ObjectMapper> objectMapperSupplier, DeserializationAction defaultEventHandler) {
        final Object array = JddlReflectionUtils.createArrayInstance(elementClass, arrayNode.size());
        for (int i = 0; i < arrayNode.size(); ++i) {
            final JsonNode eltNode = arrayNode.get(i);
            final Object elt;
            try {
                DeserializationResult res = defaultEventHandler.deserialize(new DeserializationEventImpl(fieldName, eltNode, arrayNode, elementClass, objectMapperSupplier, defaultEventHandler));
                elt = res.getValue();
            } catch (final Exception e) {
                throw new JddlException(
                        String.format("Can't deserialize element index=[%d] of array fieldName=[%s] element class=[%s]", i, fieldName, elementClass), e);
            }
            try {
                Array.set(array, i, elt);
            } catch (final Exception e) {
                throw new JddlCanNotDeserializeCollectionException(String.format("Can't set element index=[%d] of array fieldName=[%s]", i, fieldName), e);
            }
        }
        return array;
    }

    static Object deserializeIntoCollection(ArrayNode arrayNode, Class<?> collectionClass, Class<?> elementClass,
            String fieldName, Supplier<ObjectMapper> objectMapperSupplier, DeserializationAction defaultEventHandler)
    {
        return fillInCollection(arrayNode, elementClass, fieldName, (Collection) JddlReflectionUtils.createNewInstance(collectionClass), objectMapperSupplier, defaultEventHandler);
    }

    @SuppressWarnings("unchecked")
    static Collection<?> fillInCollection(ArrayNode arrayNode, Class<?> elementClass, String fieldName,
            Collection collection, Supplier<ObjectMapper> objectMapperSupplier, DeserializationAction defaultEventHandler)
    {
        final Iterator<JsonNode> eltIterator = arrayNode.elements();
        int i = 0;
        while (eltIterator.hasNext()) {
            final JsonNode eltNode = eltIterator.next();
            final Object elt;
            try {
                DeserializationResult res = defaultEventHandler.deserialize(new DeserializationEventImpl(fieldName, eltNode, arrayNode, elementClass, objectMapperSupplier, defaultEventHandler));
                elt = res.getValue();
            } catch (final Exception e) {
                throw new JddlCanNotDeserializeObjectException(
                        String.format("Error deserializing element index=[%d] fieldName=[%s]", i, fieldName), e);
            }
            collection.add(elt);
            ++i;
        }

        return collection;
    }

}
