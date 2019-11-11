package com.company.service;

import com.company.entity.User;
import com.company.repository.UserRepository;
import com.company.repository.UserRepositoryImpl;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserServiceImpl implements UserService {

    private Gson gson;
    private UserRepository userRepository;

    public UserServiceImpl(){
        gson = new Gson();
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public List<String> findAll() {
        List<User> users = userRepository.getAll();
        return users.stream()
                .map(gson::toJson)
                .collect(toList());
    }

    @Override
    public String findById(int id) {
        return gson.toJson(userRepository.getById(id));
    }

    @Override
    public List<String> findByName(String name) {
        List<User> users = userRepository.getByName(name);
        return users.stream()
                .map(gson::toJson)
                .collect(toList());
    }

    @Override
    public void addUser(BufferedReader user) {
        userRepository.addUser(gson.fromJson(user, User.class));
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateById(BufferedReader user) {
        userRepository.updateById(gson.fromJson(user, User.class));
    }
}
