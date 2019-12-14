package com.leverx.model.dog.repository;

import com.leverx.model.dog.entity.Dog;
import com.leverx.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.config.EntityManagerFactoryConfig.getEntityManager;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;

@Slf4j
public class DogRepositoryImpl implements DogRepository {

    @Override
    public Collection<Dog> findAll() {
        return DogRepository.super.findAll();
    }

    @Override
    public Optional<Dog> findById(int id) {
        return DogRepository.super.findById(id);
    }

    @Override
    public Collection<Dog> findByOwner(int ownerId) {
        return DogRepository.super.findByOwner(ownerId);
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
