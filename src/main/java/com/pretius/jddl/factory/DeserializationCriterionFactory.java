package com.pretius.jddl.factory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.pretius.jddl.JddlException;
import com.pretius.jddl.model.DeserializationCriterion;
import com.pretius.jddl.model.DeserializationCriterionElement;
import com.pretius.jddl.model.DeserializationEvent;

public final class DeserializationCriterionFactory {

    private static final Logger logger = LoggerFactory.getLogger(DeserializationCriterionFactory.class);

    
    public static DeserializationCriterion fieldNameEquals(String fieldName) {
        return (event) -> {
            logger.debug("fieldNameEquals({}), nodeType={}, fieldName={}", 
                    fieldName, event.getJsonNode().getNodeType(), event.getFieldName());
            return event.getFieldName().equals(fieldName);
        };
    }

    public static DeserializationCriterion nodeTypeEquals(JsonNodeType nodeType) {
        return (event) -> {
            logger.debug("nodeTypeEquals({}), nodeType={}, fieldName={}", 
                    nodeType, event.getJsonNode().getNodeType(), event.getFieldName());
            return event.getJsonNode().getNodeType().equals(nodeType);
        };
    }

    public static DeserializationCriterion nodeTypeIn(JsonNodeType... nodeTypes) {
        final EnumSet<JsonNodeType> nodeTypesSet = EnumSet.noneOf(JsonNodeType.class);
        nodeTypesSet.addAll(Arrays.asList(nodeTypes));
        return (event) -> {
            logger.debug("nodeTypeIn({}), nodeType={}, fieldName={}", 
                    Arrays.toString(nodeTypes), event.getJsonNode().getNodeType(), event.getFieldName());
            return nodeTypesSet.contains(event.getJsonNode().getNodeType());
        };
    }

    public static DeserializationCriterion targetClassEquals(Class<?> clazz) {
        return (event) -> {
            logger.debug("targetClassEquals({}), nodeType={}, expectedType={}", 
                    clazz, event.getJsonNode().getNodeType(), event.getExpectedType());
            return event.getExpectedType().getTypeName().equals(clazz.getName());
        };
    }

    public static DeserializationCriterion targetClassIn(Class<?>... classes) {
        return (event) -> {
            logger.debug("targetClassIn({}), nodeType={}, expectedType={}", 
                    Arrays.toString(classes), event.getJsonNode().getNodeType(), event.getExpectedType());
            return Stream.of(classes)
                .map(Class<?>::getName)
                .anyMatch(s -> s.equals(event.getExpectedType().getTypeName()));
        };
    }
    
    public static DeserializationCriterion targetClassIsAssignableFrom(Class<?> clazz) {
        return (event) -> {
            Type expectedType = event.getExpectedType();
            logger.debug("targetClassIsAssignableFrom({}), nodeType={}, expectedType={}", 
                    clazz, event.getJsonNode().getNodeType(), expectedType);
            if (expectedType instanceof Class) {
                return ((Class<?>) expectedType).isAssignableFrom(clazz);
            } else if (expectedType instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) expectedType).getRawType();
                if (rawType instanceof Class) {
                    return ((Class<?>) rawType).isAssignableFrom(clazz);
                }
            }
            throw new JddlException("Unexpected type=["+expectedType+"]"
                    + " of class=["+expectedType.getClass()+"] passed in event");
        };
    }

    public static DeserializationCriterion targetClassIsEnum() {
        return (event) -> {
            Type expectedType = event.getExpectedType();
            logger.debug("targetClassIsEnum(), nodeType={}, expectedType={}",
                    event.getJsonNode().getNodeType(), expectedType);
            if (expectedType instanceof Class) {
                return ((Class<?>) expectedType).isEnum();
            }
            return false;
        };
    }

    public static DeserializationCriterion fieldBooleanEquals(String fieldName, Boolean fieldValue) {
        return DeserializationCriterionElement.buildCriterion(
                    debug((event)->logger.debug("fieldBooleanEquals({},{}), nodeType={}",
                            fieldName, fieldValue, event.getJsonNode().getNodeType())),
                    nullValuesCheck(fieldValue),
                    typeCheck(JsonNodeType.BOOLEAN),
                    (event) -> event.getJsonNode().booleanValue() && fieldValue
                );
    }

    public static DeserializationCriterion fieldTextEquals(String fieldName, String fieldValue) {
        return DeserializationCriterionElement.buildCriterion(
                debug((event)->logger.debug("fieldTextEquals({},{}), nodeType={}",
                        fieldName, fieldValue, event.getJsonNode().getNodeType())),
                    nullValuesCheck(fieldValue),
                    typeCheck(JsonNodeType.OBJECT, JsonNodeType.POJO),
                    fieldExists(fieldName),
                    (event) -> Objects.equals(fieldValue, event.getJsonNode().get(fieldName).asText(null))
               );
    }

    public static DeserializationCriterion fieldNumberEquals(String fieldName, BigDecimal fieldValue) {
        return DeserializationCriterionElement.buildCriterion(
                debug((event)->logger.debug("fieldNumberEquals({},{}), nodeType={}",
                        fieldName, fieldValue, event.getJsonNode().getNodeType())),
                    nullValuesCheck(fieldValue),
                    typeCheck(JsonNodeType.NUMBER),
                    (event) -> fieldValue.compareTo(new BigDecimal(event.getJsonNode().get(fieldName).asText())) == 0
                );
    }

    private static DeserializationCriterionElement debug(Consumer<DeserializationEvent> logFunction) {
        return (event) -> {
            logFunction.accept(event);
            return null;
        };
    }

    
    private static DeserializationCriterionElement fieldExists(String fieldName) {
        return (event) -> {
            if (event.getJsonNode().get(fieldName) == null) {
                return false;
            }
            return null;
        };
    }
    
    private static DeserializationCriterionElement typeCheck(JsonNodeType... nodeTypes) {
        return (event) -> {
            boolean match = Stream.of(nodeTypes)
                    .anyMatch(nt -> nt == event.getJsonNode().getNodeType());
            if (!match) {
                return false;
            }
            return null;
        };
    }

    private static DeserializationCriterionElement nullValuesCheck(Object fieldValue) {
        return (event) -> {
            JsonNode node = event.getJsonNode();
            if (node.isNull()) {
                return fieldValue == null;
            }
            if (fieldValue == null) {
                return false;
            }
            return null;
        };
    }

    
    
    private DeserializationCriterionFactory() {}

}
