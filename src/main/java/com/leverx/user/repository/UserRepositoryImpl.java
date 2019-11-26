package com.leverx.user.repository;

import com.leverx.user.entity.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import java.util.Collection;

import static com.leverx.config.HibernateConfig.getSessionFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = getLogger(UserRepositoryImpl.class);
    private final SessionFactory sessionFactory = getSessionFactory();

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findAll() {
        var session = sessionFactory.openSession();
        var query = session.createQuery("from User");
        var users = query.list();
        session.close();
        LOGGER.debug("Were received {} users", users.size());

        return users;
    }

    @Override
    public User findById(int id) {
        var session = sessionFactory.openSession();
        var user = session.get(User.class, id);
        session.close();
        LOGGER.debug("User with id = {} was received", id);

        return user;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findByName(String name) {
        var session = sessionFactory.openSession();
        var query = session.createQuery("from User where name=:name")
                .setParameter("name", name);
        var users = query.list();
        session.close();
        LOGGER.debug("Were received {} users with name = {}", users.size(), name);

        return users;
    }

    @Override
    public User save(User user) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
        LOGGER.debug("User was saved");

        return user;
    }

    @Override
    public void deleteById(int id) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        var query = session.createQuery("delete User where id=:id")
                .setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
        session.close();
        LOGGER.debug("User with id = {} was deleted", id);
    }

    @Override
    public User update(User user) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
        LOGGER.debug("User with id = {} was updated", user.getId());

        return user;
    }
}