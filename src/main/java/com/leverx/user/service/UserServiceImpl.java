package com.leverx.user.service;

import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static com.leverx.utils.ServiceUtils.convertUserDtoToUser;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;
import static org.slf4j.LoggerFactory.getLogger;

public class UserServiceImpl implements UserService {

    private static Logger LOGGER = getLogger(UserServiceImpl.class);
    private UserRepository userRepository = new UserRepositoryImpl();
    private CatRepository catRepository = new CatRepositoryImpl();

    @Override
    public User findByIdWithCats(int id) {
        var user = this.findByIdWithoutCats(id);
        var cats = catRepository.findByOwner(id);
        user.setCats(cats);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = userRepository.findAll();
        LOGGER.debug("Were received {} users", users.size());
        return users;
    }

    @Override
    public User findByIdWithoutCats(int id) {
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
        var savingPossible = isValid(userDto);
        if (savingPossible) {
            User user = convertUserDtoToUser(userDto);
            userRepository.save(user);
            LOGGER.debug("User with name = {} was saved", userDto.getName());
            return user;
        } else {
            LOGGER.error("User with name = {} was not saved", userDto.getName());
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void deleteById(String id) {
        if (isParsable(id)) {
            int parsedId = parseInt(id);
            userRepository.deleteById(parsedId);
            LOGGER.debug("User with id = {} was removed", id);
        } else {
            LOGGER.debug("User was not updated. Check if id was correct");
        }
    }

    @Override
    public User updateById(String id, UserDto userDto) {
        var userId = parseInt(id);
        User user = convertUserDtoToUser(userId, userDto);
        var updatingPossible = isValid(userDto);
        if (updatingPossible) {
            userRepository.updateById(user);
            LOGGER.debug("User with id = {} was updated", id);
            return user;
        } else {
            LOGGER.error("User with id = {} was not updated", id);
            throw new InternalServerErrorException();
        }
    }
}
