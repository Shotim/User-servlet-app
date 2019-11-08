package com.company.service;

import com.company.entity.User;

import java.util.List;

public interface UserService {

    List<String> findAll();

    String findById(int id);

    void addUser(User user);

    void deleteById(int id);
}
