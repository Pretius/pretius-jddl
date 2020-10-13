package com.pretius.jddl.model;

import java.lang.reflect.Type;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface DeserializationEvent {
    
    String getFieldName();
    
    JsonNode getJsonNode();
    
    JsonNode getParentJsonNode();

    
    /** the typethat the deserializer expects here by either getting it from the field declaration of an object or any other internal source. 
     * @return
     */
    Type getExpectedType();
    
    Supplier<ObjectMapper> getObjectMapperSupplier();
    
    DeserializationAction getEventHandle();

    Type getContainerType();


}
