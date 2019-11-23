package com.leverx.user.service;

import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<User> findAll();

    User findByIdWithoutCats(int id);

    User findByIdWithCats(int id);

    Collection<User> findByName(String name);

    User save(UserDto user);

    void deleteById(String id);

    User updateById(String id, UserDto user);
}
