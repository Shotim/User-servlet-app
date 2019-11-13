package com.leverx.user.repository;

import com.leverx.user.DTOUser;
import com.leverx.user.entity.User;

import java.util.Collection;

public interface UserRepository {

    Collection<User> findAll();

    User findById(int id);

    Collection<User> findByName(String name);

    void save(DTOUser user);

    void deleteById(String id);

    void updateById(String id, DTOUser user);
}
