package jddl.remove.me;

public class PrimitiveClassTest {

    static class Foo{
       public int a;
    }
    
    public static void main(String[] args) throws NoSuchFieldException, SecurityException {
        Class<?> intClass = Foo.class.getField("a").getType();
        System.out.println("obj.int field class: "+intClass);
        System.out.println("Integer.TYPE class:  "+Integer.TYPE);
        System.out.println("are they equal?      "+Integer.TYPE.equals(intClass));
    }
}
