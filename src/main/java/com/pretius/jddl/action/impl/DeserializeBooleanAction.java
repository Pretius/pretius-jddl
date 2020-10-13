package com.pretius.jddl.action.impl;

import java.io.IOException;
import java.lang.reflect.Type;

import com.pretius.jddl.JddlCanNotDeserializeObjectException;
import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationEvent;
import com.pretius.jddl.model.DeserializationResult;
import com.pretius.jddl.model.impl.DeserializationResultImpl;

public class DeserializeBooleanAction {

    // declared here to make the lambda's actual class visible in logs
    public static DeserializationAction getLambda() {
        return DeserializeBooleanAction::deserialize;
    }

    public static DeserializationResult deserialize(DeserializationEvent event)
            throws InstantiationException, IllegalAccessException, IOException
    {
        Type expectedType = event.getExpectedType();
        if (Boolean.class.equals(expectedType) || Boolean.TYPE.equals(expectedType)) {
            return new DeserializationResultImpl(event.getFieldName(), Boolean.valueOf(event.getJsonNode().asText()));
        } else {
            throw new JddlCanNotDeserializeObjectException("Requires a boolean or Boolean class type for deserialized field type, Class=["+expectedType+"] is not either");
        }
        
    }
    
}
