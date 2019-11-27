package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.config.HibernateConfig.getSessionFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class CatRepositoryImpl implements CatRepository {

    private static final Logger LOGGER = getLogger(CatRepositoryImpl.class);
    private final SessionFactory sessionFactory = getSessionFactory();

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findAll() {
        try (var session = sessionFactory.openSession()) {

            var query = session.createQuery("from Cat");
            var cats = query.list();
            LOGGER.debug("Were received {} cats", cats.size());
            return cats;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        try (var session = sessionFactory.openSession()) {

            var query = session.createQuery("from Cat where owner.id=:ownerId")
                    .setParameter("ownerId", ownerId);
            var cats = query.list();
            LOGGER.debug("Were received {} cats with ownerId = {}", cats.size(), ownerId);
            return cats;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public Cat findById(int id) {
        try (var session = sessionFactory.openSession()) {

            var cat = session.get(Cat.class, id);
            LOGGER.debug("Was received cat with id = {}", id);
            return cat;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }

    }

    @Override
    public Cat save(Cat cat) {
        try (var session = sessionFactory.openSession()) {

            var transaction = session.beginTransaction();
            session.save(cat);
            transaction.commit();
            LOGGER.debug("Cat was saved");
            return cat;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }
}