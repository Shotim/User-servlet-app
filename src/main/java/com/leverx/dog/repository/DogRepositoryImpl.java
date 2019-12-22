package com.leverx.dog.repository;

import com.leverx.dog.entity.Dog;
import com.leverx.dog.entity.Dog_;
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

import static com.leverx.config.EntityManagerFactoryConfig.getEntityManager;
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
            var dogs= getAllDogs(entityManager);
            transaction.commit();
            log.debug("{} dogswere found in db", dogs.size());
            return dogs;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
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
            var dog = getDogById(id, entityManager);
            transaction.commit();
            log.debug("Dog with id = {} was found in db", id);
            return Optional.of(dog);
        } catch (NoResultException e) {
            commitTransactionIfActive(transaction);
            log.debug("Dog with id = {} was not found in db", id);
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
    public Collection<Dog> findByOwner(int ownerId) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var dogs= retrieveDogsByOwner(ownerId, entityManager);
            transaction.commit();
            log.debug("Dog with ownerId = {} were found in db", ownerId);
            return dogs;
        } catch (RuntimeException e) {
            rollbackTransactionIfActive(transaction);
            log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Dog> save(Dog cat) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            entityManager.persist(cat);
            transaction.commit();
            log.debug("Dog was saved");
            return Optional.of(cat);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            rollbackTransactionIfActive(transaction);
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    private static CriteriaQuery<Dog> getDogCriteriaQueryEqualToIdParameter(int id, EntityManager entityManager, SingularAttribute<Pet, ?> attribute) {

        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Dog.class);
        var root = criteriaQuery.from(Dog.class);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, id);

        criteriaQuery.where(equalCondition);

        return criteriaQuery;
    }

    private static List<Dog> getResultList(EntityManager entityManager, CriteriaQuery<Dog> criteriaQuery) {
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private static CriteriaQuery<Dog> getDogCriteriaQuery(EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(Dog.class);
    }

    private static List<Dog> getAllDogs(EntityManager entityManager) {
        var criteriaQuery = getDogCriteriaQuery(entityManager);
        var root = criteriaQuery.from(Dog.class);

        criteriaQuery.select(root);

        return getResultList(entityManager, criteriaQuery);
    }

    private static Dog getDogById(int id, EntityManager entityManager) {
        var criteriaQuery = getDogCriteriaQueryEqualToIdParameter(id, entityManager, Pet_.id);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    private static Collection<Dog> retrieveDogsByOwner(int ownerId, EntityManager entityManager) {
        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(Dog.class);

        var root = criteriaQuery.from(Dog.class);
        var users = root.join(Dog_.owners);
        var idEqualToOwnerId = builder.equal(users.get(User_.id), ownerId);
        criteriaQuery.select(root)
                .where(idEqualToOwnerId);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
