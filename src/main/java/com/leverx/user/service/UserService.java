package com.leverx.user.service;

import com.leverx.user.entity.DTOUser;
import com.leverx.user.entity.User;

import java.util.Collection;

public interface UserService {

    Collection<User> findAll();

    User findById(int id);

    Collection<User> findByName(String name);

    void save(DTOUser user);

    void deleteById(String id);

    void updateById(String id, DTOUser user);
}
