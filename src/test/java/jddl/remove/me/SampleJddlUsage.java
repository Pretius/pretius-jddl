package jddl.remove.me;

import com.pretius.jddl.DynamicObjectDeserializer;
import com.pretius.jddl.factory.DeserializationRuleFactory;


public class SampleJddlUsage {
    
    public static abstract class ParentType {}
    public static class Type1 extends ParentType{
        String typeName;
        String fieldString;
        @Override
        public String toString() {
            return "Type1 [typeName=" + typeName + ", fieldString=" + fieldString + "]";
        }
        
    }
    public static class Type2 extends ParentType{
        String typeName;
        Double fieldDouble;
        @Override
        public String toString() {
            return "Type2 [typeName=" + typeName + ", fieldDouble=" + fieldDouble + "]";
        }
    }
    public static class Cont {
        ParentType i1;
        ParentType i2;
    }
    public static void main(String[] args) {

        // create a deserializer instance
        DynamicObjectDeserializer deserializer = new DynamicObjectDeserializer();
        
        // runtime-configure deserialization rules
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType1String", Type1.class));
        deserializer.addRule(DeserializationRuleFactory.typeByFieldValue(1, "typeName", "myType2OtherString", Type2.class));

        // deserialize a json
        String json = "{\"i1\":{\"typeName\":\"myType1String\", \"fieldString\":\"foo\"}, \"i2\":{\"typeName\":\"myType2OtherString\", \"fieldDouble\": 84.7}}";
        Cont res = deserializer.deserialize(json, Cont.class);

        // check the classes
        System.out.println(res.i1);
        System.out.println(res.i2);
        
    }

}
