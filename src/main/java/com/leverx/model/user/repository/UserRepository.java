package com.leverx.model.user.repository;

import com.leverx.model.user.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> findAll();

    Optional<User> findById(int id);

    Collection<User> findByName(String name);

    Optional<User> save(User user);

    void deleteById(int id);

    void update(User user);
}
