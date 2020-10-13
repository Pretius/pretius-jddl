package com.pretius.jddl.factory;

import java.util.function.Function;

import com.pretius.jddl.action.impl.DeserializeArrayAction;
import com.pretius.jddl.action.impl.DeserializeBooleanAction;
import com.pretius.jddl.action.impl.DeserializeEnumAction;
import com.pretius.jddl.action.impl.DeserializeNumberAction;
import com.pretius.jddl.action.impl.DeserializeObjectAction;
import com.pretius.jddl.action.impl.DeserializeStringAction;
import com.pretius.jddl.action.impl.ObjectToTypeAction;
import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.impl.DeserializationResultImpl;

public final class DeserializationActionFactory {

    public static DeserializationAction objectToType(Class<?> clazz) {
        return ObjectToTypeAction.getLambda(clazz);
    }

    public static DeserializationAction renameField(String newFieldName) {
        return (event) -> new DeserializationResultImpl(newFieldName, event.getEventHandle());
    }

    
    public static DeserializationAction renameField(String newFieldName, DeserializationAction deserializationAction) {
        return (event) -> new DeserializationResultImpl(newFieldName, deserializationAction.deserialize(event));
    }

    public static DeserializationAction stringParseFunction(Function<String,Object> stringParseFunction) {
        return (event) -> new DeserializationResultImpl(event.getFieldName(), stringParseFunction.apply(event.getJsonNode().textValue()));
    }
    
    public static DeserializationAction defaultObjectHandle() {
        return DeserializeObjectAction.getLambda();
    }

    public static DeserializationAction defaultArrayHandle() {
        return DeserializeArrayAction.getLambda();
    }

    public static DeserializationAction defaultStringHandle() {
        return DeserializeStringAction.getLambda();
    }


    public static DeserializationAction defaultEnumHandle() {
        return DeserializeEnumAction.getLambda();
    }

    public static DeserializationAction defaultBooleanHandle() {
        return DeserializeBooleanAction.getLambda();
    }

    public static DeserializationAction defaultNullHandle() {
        return (event) -> new DeserializationResultImpl(event.getFieldName(), null);
    }

    public static DeserializationAction defaultNumberHandle() {
        return DeserializeNumberAction.getLambda();
    }


    private DeserializationActionFactory() {}
    
}
