package com.leverx.user.repository;

import com.leverx.user.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> findAll();

    User findById(int id);

    Collection<User> findByName(String name);

    Optional<User> save(User user);

    void deleteById(String id);

    Optional<User> updateById(User user);
}
