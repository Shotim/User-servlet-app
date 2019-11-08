package com.company.repository;

import com.company.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface Repository {

    List<User> getAll() throws SQLException;

    User getById(int id);

    void addUser(User user);

    void deleteById(int id);
}
