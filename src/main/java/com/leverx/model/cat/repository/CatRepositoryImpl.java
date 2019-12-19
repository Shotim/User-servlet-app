package com.leverx.model.cat.repository;

import com.leverx.difactory.Injectable;
import com.leverx.exception.InternalServerErrorException;
import com.leverx.model.cat.entity.Cat;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.config.EntityManagerFactoryConfig.getEntityManager;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;

@Injectable
@Slf4j
public class CatRepositoryImpl implements CatRepository {

    @Override
    public Collection<Cat> findAll() {
        return CatRepository.super.findAll();
    }

    @Override
    public Optional<Cat> findById(int id) {
        return CatRepository.super.findById(id);
    }

    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        return CatRepository.super.findByOwner(ownerId);
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