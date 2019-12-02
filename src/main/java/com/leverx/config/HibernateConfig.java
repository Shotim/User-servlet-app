package com.leverx.config;

import lombok.NoArgsConstructor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class HibernateConfig {

    public static final String PERSISTENCE_UNIT_NAME = "Persistence";
    private static final EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
