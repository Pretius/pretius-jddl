package com.pretius.jddl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DynamicObjectDeserializerJacksonAnnotationsTests {

    private final DynamicObjectDeserializer deserializer = new DynamicObjectDeserializer();
    
    
    
    public static class T1 {
        @JsonIgnore
        String ival;
        String val;
    }
    String t1json = "{\"ival\": \"ignored val\", \"val\":\"not ignored val\"}";
    @Test
    public void deserializeObject_ignoredProperty_noValue() {
        T1 t1 = deserializer.deserialize(t1json, T1.class);
        Assertions.assertNotNull(t1);
        Assertions.assertNull(t1.ival);
        Assertions.assertNotNull(t1.val);
        Assertions.assertEquals("not ignored val", t1.val);
    }

  
    
    public static class T2 {
        @JsonProperty("valchngd")
        String valorg;
    }
    String t2json = "{\"valchngd\": \"value\"}";
    @Test
    public void deserializeObject_changedName_setValue() {
        T2 t2 = deserializer.deserialize(t2json, T2.class);
        Assertions.assertNotNull(t2);
        Assertions.assertNotNull(t2.valorg);
        Assertions.assertEquals("value", t2.valorg);
    }
    
}
