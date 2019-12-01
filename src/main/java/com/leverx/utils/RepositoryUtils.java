package com.leverx.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class RepositoryUtils {

    public static EntityTransaction beginTransaction(EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }


    public static void rollbackTransaction(EntityTransaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
