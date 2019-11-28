package com.leverx.user.repository;

import com.leverx.user.entity.User;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.config.HibernateConfig.getEntityManagerFactory;

@Slf4j
public class UserRepositoryImpl implements UserRepository {
    
    private final EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findAll() {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var query = entityManager.createQuery("from User");
            var users = query.getResultList();
            entityManager.getTransaction().commit();
            log.debug("Were received {} users", users.size());
            return users;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User findById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var user = entityManager.find(User.class, id);
            entityManager.getTransaction().commit();
            log.debug("User with id = {} was received", id);
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Collection<User> findByName(String name) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var query = entityManager.createQuery("from User where name=:name")
                    .setParameter("name", name);
            var users = query.getResultList();
            entityManager.getTransaction().commit();
            log.debug("Were received {} users with name = {}", users.size(), name);
            return users;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User save(User user) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            log.debug("User was saved");
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteById(int id) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            var query = entityManager.createQuery("delete User where id=:id")
                    .setParameter("id", id);
            query.executeUpdate();
            entityManager.getTransaction().commit();
            log.debug("User with id = {} was deleted", id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User update(User user) {
        var entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
            log.debug("User with id = {} was updated", user.getId());
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } finally {
            entityManager.close();
        }
    }
}