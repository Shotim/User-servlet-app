package com.leverx.difactory;

import com.leverx.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DIFactory {

    private static final Map<Class<?>, Object> applicationScope = new HashMap<>();
    private static final String CLASS_PATH = "com.leverx.model";
    private static Map<Class<?>, Class<?>> dependencyInjectionMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections(CLASS_PATH);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Injectable.class);
        for (Class<?> implementationClass : types) {
            for (Class<?> interfaceClass : implementationClass.getInterfaces()) {
                dependencyInjectionMap.put(interfaceClass, implementationClass);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> interfaceClass) {
        Class<?> implementationClass = dependencyInjectionMap.get(interfaceClass);
        if (applicationScope.containsKey(interfaceClass)) {
            return (T) applicationScope.get(implementationClass);
        }
        synchronized (applicationScope) {
            try {
                var inheritedClass = implementationClass.getDeclaredConstructor().newInstance();
                applicationScope.put(implementationClass, inheritedClass);
                return (T) inheritedClass;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new InternalServerErrorException(e.getMessage());
            }
        }
    }
}
