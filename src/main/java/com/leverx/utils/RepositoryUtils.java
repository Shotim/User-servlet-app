package com.leverx.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static java.util.Objects.nonNull;

public class RepositoryUtils {

    public static EntityTransaction beginTransaction(EntityManager entityManager) {
        var transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }


    public static void rollbackTransactionIfActive(EntityTransaction transaction) {
        if (nonNull(transaction) && transaction.isActive()) {
            transaction.rollback();
        }
    }

    public static void commitTransactionIfActive(EntityTransaction transaction) {
        if (nonNull(transaction) && transaction.isActive()) {
            transaction.commit();
        }
    }
}
