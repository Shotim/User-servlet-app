package com.leverx.user.service;

import java.util.List;

public interface UserService {

    List<String> findAll();

    String findById(int id);

    List<String> findByName(String name);

    void save(String user);

    void deleteById(String id);

    void updateById(String id, String user);
}
