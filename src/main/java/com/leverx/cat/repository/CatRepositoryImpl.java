package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.Cat_;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.config.HibernateConfig.getEntityManagerFactory;

@Slf4j
public class CatRepositoryImpl implements CatRepository {

    private final EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

    @Override
    public Collection<Cat> findAll() {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cat> criteriaQuery = builder.createQuery(Cat.class);

            Root<Cat> root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);

            TypedQuery<Cat> query = entityManager.createQuery(criteriaQuery);
            var cats = query.getResultList();
            transaction.commit();
            log.debug("Were received {} cats", cats.size());
            return cats;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cat> criteriaQuery = builder.createQuery(Cat.class);

            Root<Cat> root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);
            criteriaQuery.where(builder.equal(root.get(Cat_.owner), ownerId));

            TypedQuery<Cat> query = entityManager.createQuery(criteriaQuery);
            var cats = query.getResultList();
            log.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
            return cats;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cat findById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cat> criteriaQuery = builder.createQuery(Cat.class);

            Root<Cat> root = criteriaQuery.from(Cat.class);

            criteriaQuery.select(root);
            criteriaQuery.where(builder.equal(root.get(Cat_.id), id));

            TypedQuery<Cat> query = entityManager.createQuery(criteriaQuery);
            var cat = query.getSingleResult();
            transaction.commit();
            log.debug("Was received cat with id = {}", id);
            return cat;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cat save(Cat cat) {
        var entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);

            entityManager.persist(cat);
            transaction.commit();
            log.debug("Cat was saved");
            return cat;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    private EntityTransaction beginTransaction(EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }
}