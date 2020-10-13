package com.pretius.jddl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DynamicObjectDeserializerBasicTests {

    private final DynamicObjectDeserializer deserializer = new DynamicObjectDeserializer();

    
    
    public static class T1 {
        int i;
        long l;
        float f;
        double d;
        byte by;
        boolean bo;
        short s;
    }
    String t1Json = "{\"i\":1,\"l\":2,\"f\":4.4,\"d\":5.5,\"by\":6,\"bo\":true,\"s\":7}";
    @Test
    public void deserializeObject_pojoPrimitivesMatching_deserialize() {
        T1 result = deserializer.deserialize(t1Json, T1.class);
        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.i);
        Assertions.assertEquals(2, result.l);
        Assertions.assertEquals(4.4f, result.f, 0.00001f);
        Assertions.assertEquals(5.5, result.d, 0.00001);
        Assertions.assertEquals(6,result.by);
        Assertions.assertEquals(7,result.s);
    }

    
    
    public static class T2 {
        String s;
    }
    String t2Json = "{\"s\":\"string\"}";
    @Test
    public void deserializeObject_stringField_deserialize() {
        T2 result = deserializer.deserialize(t2Json, T2.class);
        
        Assertions.assertNotNull(result);
        Assertions.assertEquals("string", result.s);
    }

    
    
    public static class T4 {
        T3 t3;
    }
    public static class T3 {
        T1 t1;
        T2 t2;
    }
    String t4Json = "{\"t3\":{\"t1\":{\"i\":1,\"l\":2,\"f\":4.4,\"d\":5.5,\"by\":6,\"bo\":true,\"s\":7},\"t2\":{\"s\":\"string\"}}}";
    @Test
    public void deserializeObject_nestedClasses_deserialize() {
        T4 t4 = deserializer.deserialize(t4Json, T4.class);

        Assertions.assertNotNull(t4);
        T3 t3 = t4.t3;
        Assertions.assertNotNull(t3);
        T1 t1 = t3.t1;
        Assertions.assertNotNull(t1);
        Assertions.assertEquals(1, t1.i);
        Assertions.assertEquals(2, t1.l);
        Assertions.assertEquals(4.4f, t1.f, 0.00001f);
        Assertions.assertEquals(5.5, t1.d, 0.00001);
        Assertions.assertEquals(6,t1.by);
        Assertions.assertEquals(7,t1.s);
        T2 t2 = t3.t2;
        Assertions.assertNotNull(t2);
        Assertions.assertEquals("string", t2.s);
    }
    
    

    
    String t5json = "[1,2,3]";
    @Test
    public void deserializeObject_arrayOfPrimitives_deserialize() {
        List<Integer> res = deserializer.deserializeArray(t5json, Integer.class);
        
        Assertions.assertNotNull(res);

        Assertions.assertEquals(res.size(), 3);
        Assertions.assertEquals(res.get(0), 1);
        Assertions.assertEquals(res.get(1), 2);
        Assertions.assertEquals(res.get(2), 3);
        
    }

    public static class T6 {
        List<Integer> arr;
    }
    String t6json = "{\"arr\": [1,2,3]}";
    @Test
    public void deserializeObject_nestedArrayOfPrimitives_deserializeList() {
        T6 res = deserializer.deserialize(t6json, T6.class);

        Assertions.assertNotNull(res);
        List<Integer> arr = res.arr;

        Assertions.assertNotNull(arr);
        Assertions.assertEquals(arr.size(), 3);
        Assertions.assertEquals(arr.get(0), 1);
        Assertions.assertEquals(arr.get(1), 2);
        Assertions.assertEquals(arr.get(2), 3);
        
    }

    public static class T7 {
        Set<Integer> arr;
    }
    String t7json = "{\"arr\": [1,2,3]}";
    @Test
    public void deserializeObject_nestedArrayOfPrimitives_deserializeSet() {
        T7 res = deserializer.deserialize(t7json, T7.class);

        Assertions.assertNotNull(res);
        Set<Integer> arr = res.arr;

        Assertions.assertNotNull(arr);
        Assertions.assertEquals(arr.size(), 3);
        Assertions.assertTrue(arr.contains(1));
        Assertions.assertTrue(arr.contains(2));
        Assertions.assertTrue(arr.contains(3));
    }

    public static class T8 {
        Collection<Integer> arr;
    }
    String t8json = "{\"arr\": [1,2,3]}";
    @Test
    public void deserializeObject_nestedArrayOfPrimitives_deserializeCollection() {
        T8 res = deserializer.deserialize(t8json, T8.class);

        Assertions.assertNotNull(res);
        Collection<Integer> arr = res.arr;

        Assertions.assertNotNull(arr);
        Assertions.assertEquals(arr.size(), 3);
        Assertions.assertTrue(arr.contains(1));
        Assertions.assertTrue(arr.contains(2));
        Assertions.assertTrue(arr.contains(3));
    }

    public static class T9 {
        Integer[] arr;
    }
    String t9json = "{\"arr\": [1,2,3]}";
    @Test
    public void deserializeObject_nestedArrayOfPrimitives_deserializeArray() {
        T9 res = deserializer.deserialize(t9json, T9.class);

        Assertions.assertNotNull(res);
        Integer[] arr = res.arr;

        Assertions.assertNotNull(arr);
        Assertions.assertEquals(arr.length, 3);
        Assertions.assertTrue(arr[0]==1);
        Assertions.assertTrue(arr[1] == 2);
        Assertions.assertTrue(arr[2] == 3);
    }

    public static class T10 {
        public T10(String...strings) {
        }
        
        String testField;
    }
    String t10json = "{\"testField\": \"aaa\"}";
    @Test
    public void deserializeObject_varargConstructor_deserializeObject() {
        T10 res = deserializer.deserialize(t10json, T10.class);

        Assertions.assertNotNull(res);
        Assertions.assertEquals("aaa", res.testField);
    }
    
    
    
    
    public static enum T11Enum {
        V1, V2
    }
    public static class T11 {
        T11Enum enumVal;
    }
    String t11json = "{\"enumVal\": \"V1\"}";
    @Test
    public void deserializeObject_enumField_deserializeObject() {
        T11 res = deserializer.deserialize(t11json, T11.class);

        Assertions.assertNotNull(res);
        Assertions.assertEquals(T11Enum.V1, res.enumVal);
    }
    
  
    
    public static class T12 {
        transient String ival;
        String val;
    }
    String t12json = "{\"ival\": \"ignored val\", \"val\":\"not ignored val\"}";
    @Test
    public void deserializeObject_ignoredProperty_noValue() {
        T12 t12 = deserializer.deserialize(t12json, T12.class);
        Assertions.assertNotNull(t12);
        Assertions.assertNull(t12.ival);
        Assertions.assertNotNull(t12.val);
        Assertions.assertEquals("not ignored val", t12.val);
    }
    
    
    
}
