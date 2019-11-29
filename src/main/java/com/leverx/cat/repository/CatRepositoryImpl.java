package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManagerFactory;
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

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findAll() {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cat> cr = criteriaBuilder.createQuery(Cat.class);
            Root<Cat> root = cr.from(Cat.class);
            cr.select(root);
            TypedQuery<Cat> query = entityManager.createQuery(cr);
            var cats = query.getResultList();
            entityManager.getTransaction().commit();
            log.debug("Were received {} cats", cats.size());
            return cats;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var query = entityManager.createQuery("from Cat where owner.id=:ownerId")
                    .setParameter("ownerId", ownerId);
            var cats = query.getResultList();
            log.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
            return cats;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cat findById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var cat = entityManager.find(Cat.class, id);
            entityManager.getTransaction().commit();
            log.debug("Was received cat with id = {}", id);
            return cat;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cat save(Cat cat) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(cat);
            entityManager.getTransaction().commit();
            log.debug("Cat was saved");
            return cat;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cat update(Cat cat) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(cat);
            entityManager.getTransaction().commit();
            log.debug("Cat was updated");
            return cat;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }
}