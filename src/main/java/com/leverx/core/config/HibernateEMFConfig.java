package com.leverx.core.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

import static com.leverx.credentialsLoader.DBCredentialsLoaderProvider.getDBCredentialsLoader;
import static javax.persistence.Persistence.createEntityManagerFactory;

public class HibernateEMFConfig {

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
        var dbCredentialsLoader = getDBCredentialsLoader();
        return dbCredentialsLoader.getDBPropertiesMap();
    }

}
