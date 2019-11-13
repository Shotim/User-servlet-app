package com.leverx.user.service;

import com.google.gson.Gson;
import com.leverx.user.entity.DTOUser;
import com.leverx.user.entity.User;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;

import java.util.Collection;

public class UserServiceImpl implements UserService {

    private Gson gson;
    private UserRepository userRepository;

    public UserServiceImpl() {
        gson = new Gson();
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Collection<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void save(DTOUser user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateById(String id, DTOUser user) {
        userRepository.updateById(id, user);
    }
}
