package com.leverx.user.service;

import com.leverx.user.entity.User;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserServiceImpl implements UserService {

    private Gson gson;
    private UserRepository userRepository;

    public UserServiceImpl() {
        gson = new Gson();
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public List<String> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(gson::toJson)
                .collect(toList());

    }

    @Override
    public String findById(int id) {
        return gson.toJson(userRepository.findById(id));
    }

    @Override
    public List<String> findByName(String name) {
        List<User> users = userRepository.findByName(name);
        return users.stream()
                .map(gson::toJson)
                .collect(toList());
    }

    @Override
    public void save(BufferedReader user) {
        userRepository.save(gson.fromJson(user, User.class));
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateById(String id, BufferedReader user) {
        userRepository.updateById(id, gson.fromJson(user, User.class));
    }
}
