package com.company.service;

import com.company.entity.User;
import com.company.repository.Repository;
import com.google.gson.Gson;
import lombok.Setter;

import java.io.BufferedReader;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserServiceImpl implements UserService {

    @Setter
    private Repository repository;

    private Gson gson = new Gson();

    @Override
    public List<String> findAll() {
        List<User> users = repository.getAll();
        return users.stream().map(gson::toJson).collect(toList());
    }

    @Override
    public String findById(int id) {
        return gson.toJson(repository.getById(id));
    }

    @Override
    public void addUser(BufferedReader user) {
        repository.addUser(gson.fromJson(user,User.class));
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public void updateById(BufferedReader user) {

    }
}
