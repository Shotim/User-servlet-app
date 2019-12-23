package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.Cat_;
import com.leverx.exception.InternalServerErrorException;
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

import static com.leverx.core.config.EntityManagerFactoryConfig.getEntityManager;
import static com.leverx.utils.RepositoryUtils.beginTransaction;
import static com.leverx.utils.RepositoryUtils.commitTransactionIfActive;
import static com.leverx.utils.RepositoryUtils.rollbackTransactionIfActive;


@Slf4j
public class CatRepositoryImpl implements CatRepository {

    private static CriteriaQuery<Cat> getCatCriteriaQueryEqualToIdParameter(int id, EntityManager entityManager, SingularAttribute<Pet, ?> attribute) {

        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Cat.class);
        var root = criteriaQuery.from(Cat.class);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, id);

        criteriaQuery.where(equalCondition);

        return criteriaQuery;
    }

    private static List<Cat> getResultList(EntityManager entityManager, CriteriaQuery<Cat> criteriaQuery) {
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private static CriteriaQuery<Cat> getCatCriteriaQuery(EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(Cat.class);
    }

    private static List<Cat> getAllCats(EntityManager entityManager) {
        var criteriaQuery = getCatCriteriaQuery(entityManager);
        var root = criteriaQuery.from(Cat.class);

        criteriaQuery.select(root);

        return getResultList(entityManager, criteriaQuery);
    }

    private static Cat getCatById(int id, EntityManager entityManager) {
        var criteriaQuery = getCatCriteriaQueryEqualToIdParameter(id, entityManager, Pet_.id);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    private static Collection<Cat> retrieveCatsByOwner(int ownerId, EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Cat.class);

        var root = criteriaQuery.from(Cat.class);
        var users = root.join(Cat_.owners);
        var idEqualToOwnerId = builder.equal(users.get(User_.id), ownerId);
        criteriaQuery.select(root)
                .where(idEqualToOwnerId);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Collection<Cat> findAll() {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var cats = getAllCats(entityManager);
            transaction.commit();
            log.debug("{} Cats were found in db", cats.size());
            return cats;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
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
            var cat = getCatById(id, entityManager);
            transaction.commit();
            log.debug("Cat with id = {} was found in db", id);
            return Optional.of(cat);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Cat with id = {} was not found in db", id);
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
    public Collection<Cat> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var cats = retrieveCatsByOwner(ownerId, entityManager);
            transaction.commit();
            log.debug("Cat with ownerId = {} were found in db", ownerId);
            return cats;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
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