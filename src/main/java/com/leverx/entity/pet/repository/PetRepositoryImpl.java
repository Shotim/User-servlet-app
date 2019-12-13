package com.leverx.entity.pet.repository;

import com.leverx.entity.pet.entity.Pet;
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
public class PetRepositoryImpl implements PetRepository {

    @Override
    public Collection<Pet> findAll() {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pets = getAllPets(entityManager, Pet.class);
            log.debug("Were received {} pets", pets.size());
            transaction.commit();
            return pets;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
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
            var pet = getPetById(id, entityManager, Pet.class);
            transaction.commit();
            log.debug("Pet with id = {} was received", pet.getId());
            return Optional.of(pet);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Pet with id = {} was not found", id);
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
    public Collection<Pet> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var pets = retrievePetsByOwner(ownerId, entityManager, Pet.class);
            transaction.commit();
            return pets;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }
}
