package com.leverx.pet.repository;

import com.leverx.core.exception.InternalServerErrorException;
import com.leverx.pet.entity.Pet;
import com.leverx.pet.entity.Pet_;
import com.leverx.user.entity.User_;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.leverx.core.config.HibernateEMFConfig.getEntityManager;
import static com.leverx.core.utils.RepositoryUtils.beginTransaction;
import static com.leverx.core.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.core.utils.RepositoryUtils.rollbackTransactionIfActive;

@Slf4j
public class PetRepositoryImpl implements PetRepository {

    @Override
    public Collection<Pet> findAll() {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pets = getAllPets(entityManager);
            transaction.commit();
            log.debug("{} pets were found in db", pets.size());
            return pets;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Pet> findById(int id) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pet = getPetById(id, entityManager);
            transaction.commit();
            log.debug("Pet with id = {} was found in db", id);
            return Optional.of(pet);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Pet with id = {} was not found in db", id);
            return Optional.empty();

        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Pet> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pets = retrievePetsByOwner(ownerId, entityManager);
            transaction.commit();
            log.debug("Pet with ownerId = {} were found in db", ownerId);
            return pets;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    private CriteriaQuery<Pet> getPetCriteriaQueryEqualToIdParameter(int id, EntityManager entityManager, SingularAttribute<Pet, ?> attribute) {

        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Pet.class);
        var root = criteriaQuery.from(Pet.class);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, id);

        criteriaQuery.where(equalCondition);

        return criteriaQuery;
    }

    private List<Pet> getResultList(EntityManager entityManager, CriteriaQuery<Pet> criteriaQuery) {
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private CriteriaQuery<Pet> getPetCriteriaQuery(EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(Pet.class);
    }

    private List<Pet> getAllPets(EntityManager entityManager) {
        var criteriaQuery = getPetCriteriaQuery(entityManager);
        var root = criteriaQuery.from(Pet.class);

        criteriaQuery.select(root);

        return getResultList(entityManager, criteriaQuery);
    }

    private Pet getPetById(int id, EntityManager entityManager) {
        var criteriaQuery = getPetCriteriaQueryEqualToIdParameter(id, entityManager, Pet_.id);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    private Collection<Pet> retrievePetsByOwner(int ownerId, EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Pet.class);

        var root = criteriaQuery.from(Pet.class);
        var users = root.join(Pet_.owners);
        var idEqualToOwnerId = builder.equal(users.get(User_.id), ownerId);
        criteriaQuery.select(root)
                .where(idEqualToOwnerId);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}