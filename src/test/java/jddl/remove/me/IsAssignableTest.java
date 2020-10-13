package jddl.remove.me;

public class IsAssignableTest {

    public static void main(String[] args) {
        System.out.println("Number.class.isAssignableFrom(Integer.class): "+ Number.class.isAssignableFrom(Integer.class));
        System.out.println("Integer.class.isAssignableFrom(Number.class): "+ Integer.class.isAssignableFrom(Number.class));
        System.out.println("Integer.class.isAssignableFrom(Integer.class): "+ Integer.class.isAssignableFrom(Integer.class));
        System.out.println("Integer.class.isAssignableFrom(Double.class): "+ Integer.class.isAssignableFrom(Double.class));
        
        
    }
}
