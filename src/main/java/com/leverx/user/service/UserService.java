package com.leverx.user.service;

import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<User> findAll();

    User findById(int id);

    Collection<User> findByName(String name);

    boolean save(UserDto user);

    void deleteById(String id);

    boolean updateById(String id, UserDto user);
}
