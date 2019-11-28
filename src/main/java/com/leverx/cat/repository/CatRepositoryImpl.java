package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import org.slf4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.config.HibernateConfig.getEntityManagerFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class CatRepositoryImpl implements CatRepository {

    private static final Logger LOGGER = getLogger(CatRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findAll() {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var query = entityManager.createQuery("from Cat");
            var cats = query.getResultList();
            entityManager.getTransaction().commit();
            LOGGER.debug("Were received {} cats", cats.size());
            return cats;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
            LOGGER.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
            return cats;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
            LOGGER.debug("Was received cat with id = {}", id);
            entityManager.getTransaction().commit();
            return cat;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
            return cat;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }
}