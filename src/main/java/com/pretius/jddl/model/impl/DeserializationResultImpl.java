package com.pretius.jddl.model.impl;

import com.pretius.jddl.model.DeserializationResult;

public class DeserializationResultImpl implements DeserializationResult{

    private String fieldName;

    private Object value;
    

    public DeserializationResultImpl(String fieldName, Object value) {
        this.fieldName = fieldName;
        this.value = value;
    }
    

    @Override
    public String getFieldName() {
        return fieldName;
    }
    
    @Override
    public Object getValue() {
        return value;
    }

    
    @Override
    public String toString() {
        return "DeserializationResultImpl [fieldName=" + fieldName + ", value=" + value + "]";
    }

    public static DeserializationResultImpl of(String fieldName, Object deserializedObject) {
        return new DeserializationResultImpl(fieldName, deserializedObject);
    }

}
