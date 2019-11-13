package com.company.user.repository;

import com.company.user.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    List<User> getByName(String name);

    void save(User user);

    void deleteById(int id);

    void updateById(String id, User user);
}
