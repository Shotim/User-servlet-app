package com.company.repository;

import com.company.entity.User;

import java.util.List;

public interface Repository {

    List<User> getAll();

    User getById(int id);

    List<User> getByName(String name);

    void addUser(User user);

    void deleteById(int id);

    void updateById(User user);
}
