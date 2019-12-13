package com.leverx.config;

import com.leverx.credloader.envvar.DBEnvironmentVariableLoader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryConfig {

    private static final String PERSISTENCE_UNIT_NAME = "Persistence";
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;
    private static DBEnvironmentVariableLoader loader = new DBEnvironmentVariableLoader();
    //private DBPropertiesLoader loader = new DBPropertiesLoader();

    public static void createEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, loader.getDBProperties());
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }

}
