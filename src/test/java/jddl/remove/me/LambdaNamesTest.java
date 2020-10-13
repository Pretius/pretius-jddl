package jddl.remove.me;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;

public class LambdaNamesTest {
    private static final int COUNT = 1_000_000;
    private static boolean first = true;

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            showIdentity(() -> {
            });
        }
        String time = NumberFormat.getNumberInstance().format((double) (System.currentTimeMillis() - t) / COUNT);
        System.out.println("time per call: " + time + "ms");
    }

    public interface MethodAwareRunnable extends Runnable, Serializable {
    }

    private static void showIdentity(MethodAwareRunnable consumer) {
        consumer.run();
        String name = name(consumer);
        if (first) {
            first = false;
            Class<?> clazz = consumer.getClass();
            System.out.printf("class name     : %s%n", clazz.getName());
            System.out.printf("class hashcode : %s%n", clazz.hashCode());
            System.out.printf("canonical name : %s%n", clazz.getCanonicalName());
            System.out.printf("enclosing class: %s%n", clazz.getEnclosingClass());
            System.out.printf("lambda name    : %s%n", name);
        }
    }

    private static String name(Object consumer) {
        return method(consumer).getDeclaringClass().getName();
    }

    private static SerializedLambda serialized(Object lambda) {
        try {
            Method writeMethod = lambda.getClass().getDeclaredMethod("writeReplace");
            writeMethod.setAccessible(true);
            return (SerializedLambda) writeMethod.invoke(lambda);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getContainingClass(SerializedLambda lambda) {
        try {
            String className = lambda.getImplClass().replaceAll("/", ".");
            return Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Method method(Object lambda) {
        SerializedLambda serialized = serialized(lambda);
        Class<?> containingClass = getContainingClass(serialized);
        return Arrays.stream(containingClass.getDeclaredMethods())
                .filter(method -> Objects.equals(method.getName(), serialized.getImplMethodName())).findFirst()
                .orElseThrow(RuntimeException::new);
    }

}
