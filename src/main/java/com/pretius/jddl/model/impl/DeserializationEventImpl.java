package com.pretius.jddl.model.impl;

import java.lang.reflect.Type;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationEvent;

public class DeserializationEventImpl implements DeserializationEvent {

    private String fieldName;
    private JsonNode jsonNode;
    private JsonNode parentJsonNode;
    private Type expectedType;
    private Type containerType;
    private Supplier<ObjectMapper> objectMapperSupplier;
    private DeserializationAction eventHandle;


    public DeserializationEventImpl(String fieldName, JsonNode jsonNode, JsonNode parentJsonNode, Type expectedType,
            Supplier<ObjectMapper> objectMapperSupplier, DeserializationAction deserializeEventHandler)
    {
        this.fieldName = fieldName;
        this.jsonNode = jsonNode;
        this.parentJsonNode = parentJsonNode;
        this.expectedType = expectedType;
        this.objectMapperSupplier = objectMapperSupplier;
        this.eventHandle = deserializeEventHandler;
    }

    public DeserializationEventImpl(String fieldName, JsonNode jsonNode, JsonNode parentJsonNode, Type expectedType,
            Type containerType, Supplier<ObjectMapper> objectMapperSupplier, DeserializationAction deserializeEventHandler)
    {
        this.fieldName = fieldName;
        this.jsonNode = jsonNode;
        this.parentJsonNode = parentJsonNode;
        this.expectedType = expectedType;
        this.containerType = containerType;
        this.objectMapperSupplier = objectMapperSupplier;
        this.eventHandle = deserializeEventHandler;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public JsonNode getJsonNode() {
        return jsonNode;
    }

    @Override
    public JsonNode getParentJsonNode() {
        return parentJsonNode;
    }

    @Override
    public Type getExpectedType() {
        return expectedType;
    }
    
    @Override
    public Supplier<ObjectMapper> getObjectMapperSupplier() {
        return objectMapperSupplier;
    }

    @Override
    public DeserializationAction getEventHandle() {
        return eventHandle;
    }

    @Override
    public Type getContainerType() {
        return containerType;
    }

    @Override
    public String toString() {
        return "DeserializationEventImpl [fieldName=" + fieldName + ", jsonNode=" + jsonNode + ", expectedClass="
                + expectedType + "]";
    }
    
}
