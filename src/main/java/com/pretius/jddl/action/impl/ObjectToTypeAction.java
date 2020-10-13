package com.pretius.jddl.action.impl;

import com.pretius.jddl.model.DeserializationAction;

public class ObjectToTypeAction {

    public static DeserializationAction getLambda(Class<?> clazz) {
        return (event) -> DeserializeObjectAction.deserializeObject(event.getJsonNode(), event.getParentJsonNode(),
                clazz, event.getFieldName(), event.getObjectMapperSupplier(), event.getEventHandle());
    }

}
