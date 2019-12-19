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
    private static Map<Class<?>, Class<?>> dependencyInjectionMap = new HashMap<>();
    private static final String CLASS_PATH = "com.leverx.model";

    static {
        Reflections reflections = new Reflections(CLASS_PATH);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Injectable.class);
        for (Class<?> implementationClass : types) {
            for (Class<?> interfaceClass : implementationClass.getInterfaces()) {
                dependencyInjectionMap.put(interfaceClass, implementationClass);
            }
        }
    }

    public static Object getBean(Class<?> interfaceClass) {
        Class<?> implementationClass = dependencyInjectionMap.get(interfaceClass);
        if (applicationScope.containsKey(interfaceClass)) {
            return applicationScope.get(implementationClass);
        }
        synchronized (applicationScope) {
            try {
                var inheritedClass = implementationClass.getDeclaredConstructor().newInstance();
                applicationScope.put(implementationClass, inheritedClass);
                return inheritedClass;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new InternalServerErrorException(e.getMessage());
            }
        }
    }
}
