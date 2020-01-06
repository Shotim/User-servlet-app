package com.leverx.core.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

import static com.leverx.core.loader.DBCredentialsLoaderProvider.getDBCredentialsLoader;
import static javax.persistence.Persistence.createEntityManagerFactory;

public class HibernateEMFConfig {

    private static final String PERSISTENCE_UNIT_NAME = "Persistence";
    private static EntityManagerFactory entityManagerFactory;

    public static void getEntityManagerFactory() {
        entityManagerFactory = createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getProperties());
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    private static Map<String, String> getProperties() {
        var dbCredentialsLoader = getDBCredentialsLoader();
        return dbCredentialsLoader.getDBPropertiesMap();
    }

}
