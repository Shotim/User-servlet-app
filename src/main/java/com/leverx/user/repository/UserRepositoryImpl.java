package com.leverx.user.repository;

import com.leverx.user.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.config.HibernateConfig.getSessionFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = getLogger(UserRepositoryImpl.class);
    private final SessionFactory sessionFactory = getSessionFactory();

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findAll() {
        try (var session = sessionFactory.openSession()) {

            var query = session.createQuery("from User");
            var users = query.list();
            LOGGER.debug("Were received {} users", users.size());
            return users;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }

    }

    @Override
    public User findById(int id) {
        try (var session = sessionFactory.openSession()) {

            var user = session.get(User.class, id);
            LOGGER.debug("User with id = {} was received", id);
            return user;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findByName(String name) {
        try (var session = sessionFactory.openSession()) {

            var query = session.createQuery("from User where name=:name")
                    .setParameter("name", name);
            var users = query.list();
            LOGGER.debug("Were received {} users with name = {}", users.size(), name);
            return users;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public User save(User user) {
        try (var session = sessionFactory.openSession()) {

            var transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            LOGGER.debug("User was saved");
            return user;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void deleteById(int id) {
        try (var session = sessionFactory.openSession()) {

            var transaction = session.beginTransaction();
            var query = session.createQuery("delete User where id=:id")
                    .setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
            LOGGER.debug("User with id = {} was deleted", id);

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }

    }

    @Override
    public User update(User user) {
        try (var session = sessionFactory.openSession()) {

            var transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            LOGGER.debug("User with id = {} was updated", user.getId());
            return user;

        } catch (HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }
}