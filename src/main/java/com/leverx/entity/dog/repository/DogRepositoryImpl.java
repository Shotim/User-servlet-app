package com.leverx.entity.dog.repository;

import com.leverx.entity.dog.entity.Dog;
import com.leverx.entity.pet.entity.Pet_;
import com.leverx.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.servlet.listener.ServletListener.getEntityManager;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;

@Slf4j
public class DogRepositoryImpl implements DogRepository {

    @Override
    public Collection<Dog> findAll() {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(Dog.class);
            var root = criteriaQuery.from(Dog.class);

            criteriaQuery.select(root);

            var query = entityManager.createQuery(criteriaQuery);
            var dogs = query.getResultList();
            log.debug("Were received {} dogs", dogs.size());
            transaction.commit();
            return dogs;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Dog> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction = beginTransaction(entityManager);

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(Dog.class);
            var root = criteriaQuery.from(Dog.class);

            criteriaQuery.select(root);
            var path = root.get(Pet_.owner);
            var equalCondition = builder.equal(path, ownerId);
            criteriaQuery.where(equalCondition);

            var query = entityManager.createQuery(criteriaQuery);
            var dogs = query.getResultList();
            transaction.commit();
            log.debug("Were received {} cats with ownerId = {}", dogs.size(), ownerId);
            return dogs;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException();

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Dog> findById(int id) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();

            var builder = entityManager.getCriteriaBuilder();
            var criteriaQuery = builder.createQuery(Dog.class);
            var root = criteriaQuery.from(Dog.class);

            criteriaQuery.select(root);
            var path = root.get(Pet_.id);
            var equalCondition = builder.equal(path, id);
            criteriaQuery.where(equalCondition);

            var query = entityManager.createQuery(criteriaQuery);
            var dog = query.getSingleResult();
            transaction.commit();
            log.debug("Dog with id = {} was received", dog.getId());
            return Optional.of(dog);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Dog with id = {} was not found", id);
            return Optional.empty();

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException();

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Dog> save(Dog dog) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction = beginTransaction(entityManager);

            entityManager.persist(dog);

            transaction.commit();
            log.debug("Dog was saved");
            return Optional.of(dog);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException();

        } finally {
            entityManager.close();
        }
    }
}
