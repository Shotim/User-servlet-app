package com.leverx.utils;

import com.leverx.entity.pet.entity.Pet;
import com.leverx.entity.pet.entity.Pet_;
import com.leverx.entity.user.entity.User_;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.util.Collection;

import static java.util.Objects.nonNull;

public class RepositoryUtils {


    public static <T extends Pet, T_ extends Pet_> Collection<T> retrievePetsByOwner(int ownerId, EntityManager entityManager, Class<T> entityClass) {
        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(entityClass);

        var root = criteriaQuery.from(entityClass);
        var users = root.join(T_.owners);
        var idEqualToOwnerId = builder.equal(users.get(User_.id), ownerId);
        criteriaQuery.select(root)
                .where(idEqualToOwnerId);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

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
