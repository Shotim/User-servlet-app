package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import org.slf4j.Logger;

import javax.persistence.EntityManagerFactory;
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
        entityManager.getTransaction().begin();
        var query = entityManager.createQuery("from Cat");
        var cats = query.getResultList();
        entityManager.getTransaction().commit();
        LOGGER.debug("Were received {} cats", cats.size());
        entityManager.close();
        return cats;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var query = entityManager.createQuery("from Cat where owner.id=:ownerId")
                .setParameter("ownerId", ownerId);
        var cats = query.getResultList();
        LOGGER.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
        entityManager.close();
        return cats;
    }

    @Override
    public Cat findById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var cat = entityManager.find(Cat.class, id);
        LOGGER.debug("Was received cat with id = {}", id);
        entityManager.getTransaction().commit();
        entityManager.close();
        return cat;
    }

    @Override
    public Cat save(Cat cat) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(cat);
        entityManager.getTransaction().commit();
        entityManager.close();
        return cat;
    }
}