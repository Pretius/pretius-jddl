package com.pretius.jddl.action.impl;

import java.io.IOException;
import java.lang.reflect.Type;

import com.pretius.jddl.JddlCanNotDeserializeEnumException;
import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationEvent;
import com.pretius.jddl.model.DeserializationResult;
import com.pretius.jddl.model.impl.DeserializationResultImpl;

public class DeserializeEnumAction {

    // declared here to make the lambda's actual class visible in logs
    public static DeserializationAction getLambda() {
        return DeserializeEnumAction::deserialize;
    }

    public static DeserializationResult deserialize(DeserializationEvent event)
            throws InstantiationException, IllegalAccessException, IOException
    {
        String textValue = event.getJsonNode().textValue();
        Type expectedType = event.getExpectedType();
        if (!(expectedType instanceof Class)) {
            throw new JddlCanNotDeserializeEnumException("can not deserialize text value=["+textValue+"] to enum, Type=["+expectedType+"] is not an Enum");
        }
        Class<?> expectedClass = (Class<?>) expectedType;
        if (!expectedClass.isEnum()) {
            throw new JddlCanNotDeserializeEnumException("can not deserialize text value=["+textValue+"] to enum, Class=["+expectedClass+"] is not an Enum");
        }
        Object ret = findEnumValue(expectedClass, textValue);
        return new DeserializationResultImpl(event.getFieldName(), ret);
    }
    
    
    private static Object findEnumValue(Class<?> enumType, String value) {
        for (Object o : enumType.getEnumConstants()) {
            if (String.valueOf(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

}
