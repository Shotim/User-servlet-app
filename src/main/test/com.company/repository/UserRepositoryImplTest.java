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
        User predictable = new User(1, "Name");
        User actual = repository.getById(1);

        assertEquals(predictable, actual);
    }

    @org.junit.jupiter.api.Test
    void getByName() {
        List<User> predictable = new ArrayList<>();
        predictable.add(new User(1, "Name"));
        List<User> actual = repository.getByName("Name");
        assertEquals(predictable, actual);
    }

    @org.junit.jupiter.api.Test
    void addUser() {
        User predictable = new User(3, "Yura");
        repository.addUser(predictable);
        assertEquals(predictable, repository.getById(3));
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
        User predictable = new User();
        repository.deleteById(3);
        assertEquals(repository.getById(3), predictable);
    }

    @org.junit.jupiter.api.Test
    void updateById() {
        User predictable = new User(1,"Tima");
        repository.updateById(predictable);
        assertEquals(predictable, repository.getById(predictable.getId()));
    }
}