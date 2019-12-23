package com.leverx.config;

import com.leverx.credentialsLoader.DBCredentialsLoader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

import static java.util.ServiceLoader.load;
import static javax.persistence.Persistence.createEntityManagerFactory;

public class EntityManagerFactoryConfig {

    private static final String PERSISTENCE_UNIT_NAME = "Persistence";
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    public static void getEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY = createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getProperties());
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }

    private static Map<String, String> getProperties() {
        var loader = load(DBCredentialsLoader.class);
        var dbCredentialsLoader = loader.findFirst().orElseThrow();
        return dbCredentialsLoader.getDBProperties();
    }

}
