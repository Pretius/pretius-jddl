package com.pretius.jddl.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.pretius.jddl.JddlClassInstantiationException;

public class JddlReflectionUtils {
    
    private JddlReflectionUtils() {}

    

    public static Object createNewInstance(Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getConstructors();

        final Optional<Constructor<?>> noArgsConstructor = Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterTypes().length == 0).findAny();
        if (noArgsConstructor.isPresent()) {
            return invokeConstructor(noArgsConstructor.get());
        }

        final Optional<Constructor<?>> varArgsConstructor = Arrays.stream(constructors).filter(constructor -> {
            if (!constructor.isVarArgs()) {
                return false;
            }
            final Class<?>[] args = constructor.getParameterTypes();
            return args.length == 1 && args[0].isArray();
        }).findAny();
        if (varArgsConstructor.isPresent()) {
            Constructor<?> constructor = varArgsConstructor.get();
            return invokeConstructor(constructor,
                    createArrayInstance(constructor.getParameterTypes()[0].getComponentType(), 0));

        }

        throw new JddlClassInstantiationException("Error finding no arguments constructor in type=[" + clazz + "]");
    }

    public static Object createArrayInstance(Class<?> elementClass, int size) {
        try {
            return Array.newInstance(elementClass, size);
        } catch (final Exception e) {
            throw new JddlClassInstantiationException(
                    String.format("Error instantiating array of type=[%s] and size=[%d]", elementClass, size), e);
        }
    }

    static Object invokeConstructor(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (final Exception e) {
            throw new JddlClassInstantiationException(
                    "Error instantiating object of type=[" + constructor.getDeclaringClass()+"]", e);
        }
    }
    

    public static Set<Field> getAllFields(final Class<?> type, Predicate<? super Field>... predicates) {
        Set<Field> result = new HashSet<>();
        for (Class<?> t : getAllSuperTypes(type)) {
            result.addAll(getFields(t, predicates));
        }
        return result;
    }

    static Set<Class<?>> getAllSuperTypes(final Class<?> type, Predicate<? super Class<?>>... predicates) {
        Set<Class<?>> result = new LinkedHashSet<>();
        if (type != null && (!type.equals(Object.class))) {
            result.add(type);
            for (Class<?> supertype : getSuperTypes(type)) {
                result.addAll(getAllSuperTypes(supertype));
            }
        }
        return filter(result, predicates);
    }

    static Set<Field> getFields(Class<?> type, Predicate<? super Field>... predicates) {
        return filter(new ArrayList<Field>(Arrays.asList(type.getDeclaredFields())), predicates);
    }
    
    static <T> Set<T> filter(Iterable<T> elements, Predicate<? super T>... predicates) {
        Set<T> hashSet = new LinkedHashSet<>();

        for (Iterator<T> iterator = elements.iterator(); iterator.hasNext();) {
            T t = iterator.next();
            boolean allMatch = true;
            for (Predicate<? super T> pred : predicates) {
                if (!pred.test(t)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                hashSet.add(t);
            }
        }

        return hashSet;
    }

    static Set<Class<?>> getSuperTypes(Class<?> type) {
        Set<Class<?>> result = new LinkedHashSet<>();
        Class<?> superclass = type.getSuperclass();
        Class<?>[] interfaces = type.getInterfaces();
        if (superclass != null && (!superclass.equals(Object.class))) {
            result.add(superclass);
        }
        if (interfaces != null && interfaces.length > 0) {
            result.addAll(Arrays.asList(interfaces));
        }
        return result;
    }

}
