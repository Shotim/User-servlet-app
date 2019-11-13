package com.leverx.user.service;

import java.util.Collection;

public interface UserService {

    Collection<String> findAll();

    String findById(int id);

    Collection<String> findByName(String name);

    void save(String user);

    void deleteById(String id);

    void updateById(String id, String user);
}
