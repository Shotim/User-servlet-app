package com.leverx.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateConfig {

    private static EntityManagerFactory entityManagerFactory;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {

        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("Application");
        }

        return entityManagerFactory;
    }
}
