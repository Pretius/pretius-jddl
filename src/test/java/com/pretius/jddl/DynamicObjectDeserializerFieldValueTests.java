package com.pretius.jddl;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pretius.jddl.factory.DeserializationRuleFactory;

public class DynamicObjectDeserializerFieldValueTests {

    
    private DynamicObjectDeserializer deserializer;
    
    @BeforeEach
    void setupNewDeserializer() {
        deserializer = new DynamicObjectDeserializer();
    }
    
    
    public static class ParentType {}
    public static class Type1 extends ParentType{
        String typeName;
    }
    String t1Json = "{\"typeName\":\"myType1String\"}";
    @Test
    public void deserializeObject_simpleObjectAndFieldValueCondition_properTypeDeserialized() {
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType1String", Type1.class));
        
        ParentType res = deserializer.deserialize(t1Json, ParentType.class);
        
        Assertions.assertNotNull(res);
        Assertions.assertEquals(Type1.class, res.getClass());
        Assertions.assertEquals("myType1String", ((Type1)res).typeName);
    }

    
    
    
    public static class Type2 extends ParentType{
        String typeName;
    }
    public static class Cont {
        ParentType i1;
        ParentType i2;
    }
    String t2Json = "{\"i1\":{\"typeName\":\"myType1String\"}, \"i2\":{\"typeName\":\"myType2OtherString\"}}";
    @Test
    public void deserializeObject_nestedObjectAndFieldValueCondition_properTypesDeserialized() {
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType1String", Type1.class));
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType2OtherString", Type2.class));

        Cont res = deserializer.deserialize(t2Json, Cont.class);

        Assertions.assertNotNull(res);
        Assertions.assertNotNull(res.i1);
        Assertions.assertNotNull(res.i2);
        Assertions.assertEquals(Type1.class, res.i1.getClass());
        Assertions.assertEquals("myType1String", ((Type1)res.i1).typeName);
        Assertions.assertEquals(Type2.class, res.i2.getClass());
        Assertions.assertEquals("myType2OtherString", ((Type2)res.i2).typeName);
    }
    

    String t3Json = "[{\"typeName\":\"myType1String\"}, {\"typeName\":\"myType2OtherString\"}]";
    @Test
    public void deserializeObject_arrayAndFieldValueCondition_properTypesDeserialized() {
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType1String", Type1.class));
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType2OtherString", Type2.class));

        List<ParentType> res = deserializer.deserializeArray(t3Json, ParentType.class);

        Assertions.assertNotNull(res);
        Assertions.assertEquals(2, res.size());
        
        ParentType e0 = res.get(0);
        ParentType e1 = res.get(1);
        
        Assertions.assertNotNull(e0);
        Assertions.assertNotNull(e1);
        Assertions.assertEquals(Type1.class, e0.getClass());
        Assertions.assertEquals("myType1String", ((Type1)e0).typeName);
        Assertions.assertEquals(Type2.class, e1.getClass());
        Assertions.assertEquals("myType2OtherString", ((Type2)e1).typeName);
    }

}
