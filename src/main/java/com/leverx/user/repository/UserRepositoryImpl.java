package com.leverx.user.repository;

import com.leverx.core.exception.InternalServerErrorException;
import com.leverx.user.entity.User;
import com.leverx.user.entity.User_;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.core.config.EntityManagerFactoryConfig.getEntityManager;
import static com.leverx.core.utils.RepositoryUtils.beginTransaction;
import static com.leverx.core.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.core.utils.RepositoryUtils.rollbackTransactionIfActive;

@Slf4j

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Collection<User> findAll() {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(User.class);
            var root = criteriaQuery.from(User.class);

            criteriaQuery.select(root);

            var query = entityManager.createQuery(criteriaQuery)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE);
            var users = query.getResultList();
            transaction.commit();
            log.debug("Were received {} users", users.size());
            return users;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var criteriaQuery = getUserCriteriaQueryEqualToParameter(id, User_.id, entityManager);
            var query = entityManager.createQuery(criteriaQuery)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE);
            var user = query.getSingleResult();

            transaction.commit();
            log.debug("User with id = {} was received", id);
            return Optional.of(user);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("User with id = {} was not found", id);
            return Optional.empty();

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<User> findByName(String name) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var criteriaQuery = getUserCriteriaQueryEqualToParameter(name, User_.name, entityManager);
            var query = entityManager.createQuery(criteriaQuery)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE);
            var users = query.getResultList();

            transaction.commit();
            log.debug("Were received {} users with name = {}", users.size(), name);
            return users;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }


    @Override
    public Optional<User> save(User user) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            entityManager.persist(user);
            transaction.commit();
            log.debug("User was saved");
            return Optional.of(user);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteById(int id) {
        var entityManager = getEntityManager();
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
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(User user) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            entityManager.merge(user);
            transaction.commit();
            log.debug("User with id = {} was updated", user.getId());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public void pointsTransfer(int senderId, int recipientId, int points) {
        var entityManager = getEntityManager();
        var builder = entityManager.getCriteriaBuilder();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            addAnimalPointsToUser(recipientId, entityManager, builder, points);

            entityManager.joinTransaction();

            addAnimalPointsToUser(senderId, entityManager, builder, -points);
            transaction.commit();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    private void addAnimalPointsToUser(int userId, EntityManager entityManager, CriteriaBuilder builder, int points) {
        var criteriaUpdate = builder.createCriteriaUpdate(User.class);
        var root = criteriaUpdate.from(User.class);
        var user = builder.equal(root.get(User_.id), userId);
        var addPoints = builder.sum(root.get(User_.animalPoints), points);
        criteriaUpdate.where(user)
                .set(root.get(User_.animalPoints), addPoints);
        var query = entityManager.createQuery(criteriaUpdate);
        query.executeUpdate();
    }

    private <T> CriteriaQuery<User> getUserCriteriaQueryEqualToParameter(T parameter, SingularAttribute<User, ?> attribute, EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(User.class);
        var root = criteriaQuery.from(User.class);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, parameter);
        criteriaQuery.where(equalCondition);
        return criteriaQuery;
    }
}