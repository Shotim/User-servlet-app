package com.leverx.entity.pet.repository;

import com.leverx.entity.pet.entity.Pet;
import com.leverx.entity.pet.entity.Pet_;
import com.leverx.entity.user.entity.User_;
import com.leverx.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.leverx.config.EntityManagerFactoryConfig.getEntityManager;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;

public interface PetRepositoryI {

    @Slf4j
    final class LogHolder {
    }

    default <T extends Pet> Collection<T> findAll(Class<T> tClass) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pets = getAllPets(entityManager, tClass);
            transaction.commit();
            LogHolder.log.debug("{}" + tClass.toString() + "were found in db", pets.size());
            return pets;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            LogHolder.log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    default <T extends Pet> Optional<T> findById(int id, Class<T> tClass) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pet = getPetById(id, entityManager, tClass);
            transaction.commit();
            LogHolder.log.debug(tClass.toString() + "with id = {} was found in db", id);
            return Optional.of(pet);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            LogHolder.log.debug(tClass.toString() + "with id = {} was not found in db", id);
            return Optional.empty();

        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            LogHolder.log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    default <T extends Pet> Collection<T> findByOwner(int ownerId, Class<T> tClass) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pets = retrievePetsByOwner(ownerId, entityManager, tClass);
            transaction.commit();
            LogHolder.log.debug(tClass.toString() + "with ownerId = {} were found in db", ownerId);
            return pets;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            LogHolder.log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    private static <T extends Pet> CriteriaQuery<T> getPetCriteriaQueryEqualToIdParameter(int id, EntityManager entityManager, SingularAttribute<Pet, ?> attribute, Class<T> t) {

        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(t);
        var root = criteriaQuery.from(t);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, id);

        criteriaQuery.where(equalCondition);

        return criteriaQuery;
    }

    private static <T extends Pet> List<T> getResultList(EntityManager entityManager, CriteriaQuery<T> criteriaQuery) {
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private static <T extends Pet> CriteriaQuery<T> getPetCriteriaQuery(EntityManager entityManager, Class<T> t) {
        var builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(t);
    }

    private static <T extends Pet> List<T> getAllPets(EntityManager entityManager, Class<T> petClass) {
        var criteriaQuery = getPetCriteriaQuery(entityManager, petClass);
        var root = criteriaQuery.from(petClass);

        criteriaQuery.select(root);

        return getResultList(entityManager, criteriaQuery);
    }

    private static <T extends Pet> T getPetById(int id, EntityManager entityManager, Class<T> t) {
        var criteriaQuery = getPetCriteriaQueryEqualToIdParameter(id, entityManager, Pet_.id, t);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    private static <T extends Pet, T_ extends Pet_> Collection<T> retrievePetsByOwner(int ownerId, EntityManager entityManager, Class<T> entityClass) {
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
}
