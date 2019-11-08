package com.company.service;

import java.io.BufferedReader;
import java.util.List;

public interface UserService {

    List<String> findAll();

    String findById(int id);

    void addUser(BufferedReader user);

    void deleteById(int id);

    void updateById(BufferedReader user);
}
