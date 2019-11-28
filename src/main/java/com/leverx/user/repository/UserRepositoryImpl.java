package com.leverx.user.repository;

import com.leverx.user.entity.User;
import org.slf4j.Logger;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;

import static com.leverx.config.HibernateConfig.getEntityManagerFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = getLogger(UserRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findAll() {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var query = entityManager.createQuery("from User");
        var users = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.debug("Were received {} users", users.size());
        return users;
    }

    @Override
    public User findById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var user = entityManager.find(User.class, id);
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.debug("User with id = {} was received", id);
        return user;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findByName(String name) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var query = entityManager.createQuery("from User where name=:name")
                .setParameter("name", name);
        var users = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.debug("Were received {} users with name = {}", users.size(), name);
        return users;
    }

    @Override
    public User save(User user) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.debug("User was saved");
        return user;
    }

    @Override
    public void deleteById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var query = entityManager.createQuery("delete User where id=:id")
                .setParameter("id", id);
        query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.debug("User with id = {} was deleted", id);
    }

    @Override
    public User update(User user) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.debug("User with id = {} was updated", user.getId());
        return user;
    }
}