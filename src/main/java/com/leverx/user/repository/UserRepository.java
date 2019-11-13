package com.leverx.user.repository;

import com.leverx.user.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findById(int id);

    List<User> findByName(String name);

    void save(User user);

    void deleteById(String id);

    void updateById(String id, User user);
}
