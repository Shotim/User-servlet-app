package com.company.service;

import com.company.entity.User;
import com.company.repository.Repository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Setter
    private Gson gson;

    private Repository repository;

    @Override
    public List<String> findAll() {
        List<User> users = repository.getAll();
        return users.stream()
                .map(gson::toJson)
                .collect(toList());
    }

    @Override
    public String findById(int id) {
        return gson.toJson(repository.getById(id));
    }

    @Override
    public List<String> findByName(String name) {
        List<User> users = repository.getByName(name);
        return users.stream()
                .map(gson::toJson)
                .collect(toList());
    }

    @Override
    public void addUser(BufferedReader user) {
        repository.addUser(gson.fromJson(user, User.class));
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public void updateById(BufferedReader user) {
        repository.updateById(gson.fromJson(user, User.class));
    }
}
