package com.pretius.jddl.action.impl;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.JsonNode;
import com.pretius.jddl.JddlCanNotDeseializeNumberException;
import com.pretius.jddl.JddlCanNotDeserializeEnumException;
import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationEvent;
import com.pretius.jddl.model.DeserializationResult;
import com.pretius.jddl.model.impl.DeserializationResultImpl;

public class DeserializeNumberAction {

    // declared here to make the lambda's actual class visible in logs
    public static DeserializationAction getLambda() {
        return DeserializeNumberAction::deserialize;
    }

    public static DeserializationResult deserialize(DeserializationEvent event)
            throws InstantiationException, IllegalAccessException, IOException
    {
        JsonNode valueNode = event.getJsonNode();
        Number value = valueNode.numberValue();

        Type expectedType = event.getExpectedType();
        if (!(expectedType instanceof Class)) {
            throw new JddlCanNotDeserializeEnumException("can not deserialize number value=["+value+"] to number, Type=["+expectedType+"] is not an Class");
        }
        
        Class<?> expectedClass = (Class<?>) expectedType;

        Object retValue = null;

        if (expectedClass == Integer.class || expectedClass == Integer.TYPE) {
            retValue = value.intValue();
        } else if (expectedClass == Double.class || expectedClass == Double.TYPE) {
            retValue = value.doubleValue();
        } else if (expectedClass == Float.class || expectedClass == Float.TYPE) {
            retValue = value.floatValue();
        } else if (expectedClass == Byte.class || expectedClass == Byte.TYPE) {
            retValue = value.byteValue();
        } else if (expectedClass == Short.class || expectedClass == Short.TYPE) {
            retValue = value.shortValue();
        } else if (expectedClass == Long.class || expectedClass == Long.TYPE) {
            retValue = value.longValue();
        } else {
            throw new JddlCanNotDeseializeNumberException(
                    "Unsupported expected class=[" + expectedClass + "] for number deserializer");
        }

//        if (!expectedClass.isAssignableFrom(value.getClass())) {
//            throw new JddlCanNotDeseializeNumberException("Deserialized class mismatch for Number instance"
//                    + " for value=["+valueNode.asText()+"] with objectmapper resulting class=["+value.getClass()+"]"
//                    + ", expected class=["+expectedClass+"]");
//        }

        return new DeserializationResultImpl(event.getFieldName(), retValue);
    }

}
