package com.leverx.user.service;

import com.google.gson.Gson;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.user.service.validator.UserValidator.isValidName;
import static com.leverx.utils.ServiceUtils.convertUserDtoToUser;
import static java.lang.Integer.parseInt;
import static org.slf4j.LoggerFactory.getLogger;

public class UserServiceImpl implements UserService {

    private static Logger LOGGER = getLogger(UserServiceImpl.class);
    private Gson gson;
    private UserRepository userRepository;

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
    public User save(UserDto userDto) {
        User user = convertUserDtoToUser(userDto);
        var savingPossible = isValidName(userDto);
        if (savingPossible) {
            userRepository.save(user);
            LOGGER.debug("User with name = {} was saved", userDto.getName());
            return user;
        } else {
            LOGGER.error("User with name = {} was not saved\nThe name has more than 60 symbols", userDto.getName());
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
        LOGGER.debug("User with id = {} was removed", id);
    }

    @Override
    public User updateById(String id, UserDto userDto) {
        var userId = parseInt(id);
        User user = convertUserDtoToUser(userId, userDto);
        var updatingPossible = isValidName(userDto);
        if (updatingPossible) {
            userRepository.updateById(user);
            LOGGER.debug("User with id = {} was updated", id);
            return user;
        } else {
            LOGGER.error("User with id = {} was not updated\nThe name has more than 60 symbols", id);
            throw new InternalServerErrorException();
        }
    }
}
