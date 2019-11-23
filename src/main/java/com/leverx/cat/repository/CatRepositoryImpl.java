package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import java.util.Collection;

import static com.leverx.config.HibernateConfig.getSessionFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class CatRepositoryImpl implements CatRepository {

    private final SessionFactory sessionFactory = getSessionFactory();
    private static final Logger LOGGER = getLogger(CatRepositoryImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findAll() {
        var session = sessionFactory.openSession();
        var query = session.createQuery("from Cat");
        var cats = query.list();
        session.close();
        LOGGER.debug("Were received {} cats", cats.size());

        return cats;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        var session = sessionFactory.openSession();
        var query = session.createQuery("from Cat where owner=:ownerId")
                .setParameter("ownerId", ownerId);
        var cats = query.list();
        session.close();
        LOGGER.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);

        return cats;
    }

    @Override
    public Cat findById(int id) {
        var session = sessionFactory.openSession();
        var cat = session.get(Cat.class, id);
        session.close();
        LOGGER.debug("Was received cat with id = {}", id);

        return cat;
    }

    @Override
    public Cat save(Cat cat) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.save(cat);
        transaction.commit();
        session.close();
        LOGGER.debug("Cat was saved");

        return cat;
    }
}