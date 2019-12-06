package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.Cat_;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.servlet.listener.ServletListener.getEntityManager;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;

@Slf4j
public class CatRepositoryImpl implements CatRepository {

    @Override
    public Collection<Cat> findAll() {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(Cat.class);

            var root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);

            var query = entityManager.createQuery(criteriaQuery);
            var cats = query.getResultList();
            transaction.commit();
            log.debug("Were received {} cats", cats.size());
            return cats;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(Cat.class);

            var root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);
            var ownerPath = root.get(Cat_.owner);
            var equalOwnerIdCondition = builder.equal(ownerPath, ownerId);
            criteriaQuery.where(equalOwnerIdCondition);

            var query = entityManager.createQuery(criteriaQuery);
            var cats = query.getResultList();
            log.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
            return cats;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Cat> findById(int id) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(Cat.class);

            var root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);
            var idPath = root.get(Cat_.id);
            var equalIdCondition = builder.equal(idPath, id);
            criteriaQuery.where(equalIdCondition);

            var query = entityManager.createQuery(criteriaQuery);
            var cat = query.getSingleResult();
            transaction.commit();
            log.debug("Was received cat with id = {}", id);
            return Optional.of(cat);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Cat with id = {} was not found", id);
            return Optional.empty();
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Cat> save(Cat cat) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            entityManager.persist(cat);
            transaction.commit();
            log.debug("Cat was saved");
            return Optional.of(cat);
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Cat> update(Cat cat) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            entityManager.merge(cat);

            transaction.commit();
            log.debug("Cat with id = {} was updated", cat.getId());
            return Optional.of(cat);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }
}