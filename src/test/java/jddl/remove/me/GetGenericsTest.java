package jddl.remove.me;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class GetGenericsTest {
    public static class MyContainerClass {
        List<String> myElementsList;
    }

    public static void main(String[] args) throws Exception {
        Field field = MyContainerClass.class.getDeclaredField("myElementsList");

        ParameterizedType pt = (ParameterizedType) field.getGenericType();
        System.out.println("collection type: " + pt.getRawType().getTypeName());
        System.out.println("elt type:        " + ((Class<?>)pt.getActualTypeArguments()[0]).getName());
    }
}
