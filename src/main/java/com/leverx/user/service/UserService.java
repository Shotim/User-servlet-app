package com.leverx.user.service;

import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<User> findAll();

    User findById(int id);

    Collection<User> findByName(String name);

    void save(UserDto user);

    void deleteById(String id);

    void updateById(String id, UserDto user);
}
