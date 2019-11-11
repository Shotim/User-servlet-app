package com.company.repository;

import com.company.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryImplTest {

    private UserRepository repository = new UserRepositoryImpl();

    @org.junit.jupiter.api.Test
    void getAll() {
        List<User> predictable = new ArrayList<>();
        predictable.add(new User(1, "Name"));
        predictable.add(new User(2, "Test"));

        List<User> actual = repository.getAll();

        assertEquals(predictable, actual);
    }

    @org.junit.jupiter.api.Test
    void getById() {
    }

    @org.junit.jupiter.api.Test
    void getByName() {
    }

    @org.junit.jupiter.api.Test
    void addUser() {
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
    }

    @org.junit.jupiter.api.Test
    void updateById() {
    }
}