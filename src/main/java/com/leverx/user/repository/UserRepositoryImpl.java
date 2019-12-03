package com.leverx.user.repository;

import com.leverx.user.entity.User;
import com.leverx.user.entity.User_;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.config.EntityManagerFactoryImpl.getEntityManagerFactory;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

    @Override
    public Collection<User> findAll() {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(User.class);

            var root = criteriaQuery.from(User.class);

            criteriaQuery.select(root);

            var query = entityManager.createQuery(criteriaQuery);
            var users = query.getResultList();
            transaction.commit();
            log.debug("Were received {} users", users.size());
            return users;
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Users were not found");
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User findById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(User.class);

            var root = criteriaQuery.from(User.class);

            criteriaQuery.select(root);
            var idPath = root.get(User_.id);
            var equalIdCondition = builder.equal(idPath, id);
            criteriaQuery.where(equalIdCondition);

            var query = entityManager.createQuery(criteriaQuery);
            var user = query.getSingleResult();
            transaction.commit();
            log.debug("User with id = {} was received", id);
            return user;
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("User with id = {} was not found", id);
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<User> findByName(String name) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(User.class);

            var root = criteriaQuery.from(User.class);

            criteriaQuery.select(root);
            var namePath = root.get(User_.name);
            var equalNameCondition = builder.equal(namePath, name);
            criteriaQuery.where(equalNameCondition);

            var query = entityManager.createQuery(criteriaQuery);
            var users = query.getResultList();
            transaction.commit();
            log.debug("Were received {} users with name = {}", users.size(), name);
            return users;
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("User with name = {} was not found", name);
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User save(User user) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            entityManager.persist(user);
            transaction.commit();
            log.debug("User was saved");
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaDelete = builder.createCriteriaDelete(User.class);

            var root = criteriaDelete.from(User.class);

            var idPath = root.get(User_.id);
            var equalIdCondition = builder.equal(idPath, id);
            criteriaDelete.where(equalIdCondition);

            var query = entityManager.createQuery(criteriaDelete);
            query.executeUpdate();
            transaction.commit();
            log.debug("User with id = {} was deleted", id);
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User update(User user) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            entityManager.merge(user);

            transaction.commit();
            log.debug("User with id = {} was updated", user.getId());
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }
}