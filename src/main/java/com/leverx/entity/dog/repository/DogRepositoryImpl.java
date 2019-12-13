package com.leverx.entity.dog.repository;

import com.leverx.entity.dog.entity.Dog;
import com.leverx.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.config.EntityManagerFactoryConfig.getEntityManager;
import static com.leverx.entity.pet.repository.utils.PetRepositoryUtils.getAllPets;
import static com.leverx.entity.pet.repository.utils.PetRepositoryUtils.getPetById;
import static com.leverx.entity.pet.repository.utils.PetRepositoryUtils.retrievePetsByOwner;
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
            var dogs = getAllPets(entityManager, Dog.class);
            log.debug("Were received {} dogs", dogs.size());
            transaction.commit();
            return dogs;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Dog> findById(int id) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var dog = getPetById(id, entityManager, Dog.class);
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
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Dog> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var dogs = retrievePetsByOwner(ownerId, entityManager, Dog.class);
            transaction.commit();
            return dogs;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Dog> save(Dog dog) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            entityManager.persist(dog);
            transaction.commit();
            log.debug("Dog was saved");
            return Optional.of(dog);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }
}
