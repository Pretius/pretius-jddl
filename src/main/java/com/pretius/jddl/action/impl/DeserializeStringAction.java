package com.pretius.jddl.action.impl;

import java.io.IOException;

import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationEvent;
import com.pretius.jddl.model.DeserializationResult;
import com.pretius.jddl.model.impl.DeserializationResultImpl;

public class DeserializeStringAction {

    // declared here to make the lambda's actual class visible in logs
    public static DeserializationAction getLambda() {
        return DeserializeStringAction::deserialize;
    }

    public static DeserializationResult deserialize(DeserializationEvent event)
            throws InstantiationException, IllegalAccessException, IOException
    {
        String textValue = event.getJsonNode().textValue();
        return new DeserializationResultImpl(event.getFieldName(), textValue);
    }
    
}
