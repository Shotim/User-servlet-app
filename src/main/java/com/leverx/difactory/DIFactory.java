package com.leverx.difactory;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DIFactory {

    private static Map<Class, Class> dependencyInjectionMap = new HashMap<>();

    private static Map<Class, Object> applicationScope = new HashMap<>();

    static {
        Reflections reflections = new Reflections("");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Injectable.class);
        for (Class<?> implementationClass : types) {
            for (Class iface : implementationClass.getInterfaces()) {
                dependencyInjectionMap.put(iface, implementationClass);
            }
        }
    }

    public static Object getBean(Class interfaceClass){
        Class implementationClass = dependencyInjectionMap.get(interfaceClass);
        if (applicationScope.containsKey(interfaceClass)) {
            return applicationScope.get(implementationClass);
        }
        synchronized (applicationScope) {
            Object inheritedClass = null;
            try {
                inheritedClass = implementationClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getMessage());
            }
            applicationScope.put(implementationClass, inheritedClass);
            return inheritedClass;
        }
    }
}
