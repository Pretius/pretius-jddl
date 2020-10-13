# JSON Dynamic Deserialization Library

Json deserialization library with runtime configured, modifiable and dynamic deserialization rules, utilizing [jackson](https://github.com/FasterXML/jackson) for low-level operations.

JDDL is distributed under [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

# Installation

The library is distributed as a jar with all classes defined in `com.pretius.jddl` package. The file is available for download from Maven repository.

To use JDDL use the following Maven dependency:

```
<dependency>
    <groupId>com.pretius</groupId>
    <artifactId>jddl</artifactId>
    <version>${jddl-version}</version>
</dependency>
```

# Usage

A simple plymorphic deserialization using single String field value to determine the class of the deserialized object at runtime.

```
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
```

# Contributing

Pull requests are more than welcome. Factory classes/methods for criteria and actions from your projects would be especially useful.
