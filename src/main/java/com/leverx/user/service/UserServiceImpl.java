package com.leverx.user.service;

import com.google.gson.Gson;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;

import java.util.Collection;

import static java.lang.Integer.parseInt;

public class UserServiceImpl implements UserService {

    private Gson gson;
    private UserRepository userRepository;
    private static final Integer DEFAULT_ID = 0;

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
    public void save(UserDto userDto) {
        User user = convertUserDtoToUser(DEFAULT_ID, userDto);
        userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateById(String id, UserDto userDto) {
        var userId = parseInt(id);
        User user = convertUserDtoToUser(userId, userDto);
        userRepository.updateById(user);
    }

    private User convertUserDtoToUser(int id, UserDto userDto) {
        String name = userDto.getName();
        return new User(id, name);
    }
}
