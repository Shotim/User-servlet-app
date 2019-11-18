package com.leverx.user.service;

import com.google.gson.Gson;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.leverx.utils.ServiceUtils.convertUserDtoToUser;
import static java.lang.Integer.parseInt;

public class UserServiceImpl implements UserService {

    private Gson gson;
    private UserRepository userRepository;
    private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
        gson = new Gson();
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = userRepository.findAll();
        LOGGER.debug("Were received {} users", users.size());
        return users;
    }

    @Override
    public User findById(int id) {
        User user = userRepository.findById(id);
        LOGGER.debug("Was received user with id = {}", id);
        return user;
    }

    @Override
    public Collection<User> findByName(String name) {
        Collection<User> users = userRepository.findByName(name);
        LOGGER.debug("Were received {} users", users.size());
        return users;
    }

    @Override
    public void save(UserDto userDto) {
        User user = convertUserDtoToUser(userDto);
        userRepository.save(user);
        LOGGER.debug("User with name = {} was saved", userDto.getName());
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
        LOGGER.debug("User with id = {} was removed", id);
    }

    @Override
    public void updateById(String id, UserDto userDto) {
        var userId = parseInt(id);
        User user = convertUserDtoToUser(userId, userDto);
        userRepository.updateById(user);
        LOGGER.debug("User with id = {} was updated", id);
    }
}
