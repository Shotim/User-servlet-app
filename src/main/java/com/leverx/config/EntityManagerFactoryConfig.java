package com.leverx.config;

import com.leverx.credloader.DBCredentialsLoader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;
import java.util.ServiceLoader;

public class EntityManagerFactoryConfig {

    private static final String PERSISTENCE_UNIT_NAME = "Persistence";
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    public static void createEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getProperties());
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }

    private static Map<String, String> getProperties() {
        var loader = ServiceLoader.load(DBCredentialsLoader.class);
        var dbCredentialsLoader = loader.findFirst().orElseThrow();
        return dbCredentialsLoader.getDBProperties();
    }

}
