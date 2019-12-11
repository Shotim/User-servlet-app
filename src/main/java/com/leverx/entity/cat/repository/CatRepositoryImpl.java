package com.leverx.entity.cat.repository;

import com.leverx.entity.cat.entity.Cat;
import com.leverx.entity.cat.entity.Cat_;
import com.leverx.entity.pet.entity.Pet;
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

            var criteriaQuery = getCatCriteriaQuery(entityManager);
            var root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);

            var cats = getResultList(entityManager, criteriaQuery);

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
    public Collection<Cat> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            var criteriaQuery = getCatCriteriaQueryEqualToIdParameter(ownerId, entityManager, Cat_.owner);
            var cats = getResultList(entityManager, criteriaQuery);
            log.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
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

            var criteriaQuery = getCatCriteriaQueryEqualToIdParameter(id, entityManager, Cat_.id);
            var query = entityManager.createQuery(criteriaQuery);
            var cat = query.getSingleResult();

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

    private CriteriaQuery<Cat> getCatCriteriaQueryEqualToIdParameter(int id, EntityManager entityManager, SingularAttribute<Pet, ?> attribute) {

        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Cat.class);
        var root = criteriaQuery.from(Cat.class);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, id);

        criteriaQuery.where(equalCondition);

        return criteriaQuery;
    }

    private List<Cat> getResultList(EntityManager entityManager, CriteriaQuery<Cat> criteriaQuery) {
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private CriteriaQuery<Cat> getCatCriteriaQuery(EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(Cat.class);
    }
}