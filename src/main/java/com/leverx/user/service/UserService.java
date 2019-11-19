package com.leverx.user.service;

import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> findAll();

    User findById(int id);

    Collection<User> findByName(String name);

    Optional<User> save(UserDto user);

    void deleteById(String id);

    Optional<User> updateById(String id, UserDto user);
}
