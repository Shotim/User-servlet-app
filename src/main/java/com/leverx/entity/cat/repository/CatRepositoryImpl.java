package com.leverx.entity.cat.repository;

import com.leverx.entity.cat.entity.Cat;
import com.leverx.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.entity.pet.repository.utils.PetRepositoryUtils.getAllPets;
import static com.leverx.entity.pet.repository.utils.PetRepositoryUtils.getPetById;
import static com.leverx.entity.pet.repository.utils.PetRepositoryUtils.retrievePetsByOwner;
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
            var cats = getAllPets(entityManager, Cat.class);
            transaction.commit();
            log.debug("Were received {} cats", cats.size());
            return cats;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

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
            var cat = getPetById(id, entityManager, Cat.class);
            transaction.commit();
            log.debug("Was received cat with id = {}", id);
            return Optional.of(cat);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Cat with id = {} was not found", id);
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
    public Collection<Cat> findByOwner(int ownerId) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var cats = retrievePetsByOwner(ownerId, entityManager, Cat.class);
            transaction.commit();
            return cats;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

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
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }
}