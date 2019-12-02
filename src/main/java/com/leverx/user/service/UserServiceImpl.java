package com.leverx.user.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.leverx.utils.ServiceUtils.convertUserDtoToUser;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepositoryImpl();
    private CatRepository catRepository = new CatRepositoryImpl();

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
    public User save(UserDto userDto) {
        if (isValid(userDto)) {
            User user = convertUserDtoToUser(userDto);
            userRepository.save(user);
            return user;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteById(String id) {
        if (isParsable(id)) {
            int parsedId = parseInt(id);
            userRepository.deleteById(parsedId);
        } else {
            log.debug("User was not updated. Check if id was correct");
        }
    }

    @Override
    public User updateById(String id, UserDto userDto) {
        var userId = parseInt(id);
        User user = convertUserDtoToUser(userId, userDto);
        if (isValid(userDto)) {
            userRepository.update(user);
            return user;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void assignCatsToUser(int ownerId, List<Integer> catsIds) {
        var user = userRepository.findById(ownerId);
        var cats = catsIds.stream()
                .map(this::findCatIfExist)
                .filter(Objects::nonNull)
                .filter(cat -> cat.getOwner() == null)
                .collect(toSet());

        cats.addAll(user.getCats());
        user.setCats(cats);
        cats.forEach(cat -> cat.setOwner(user));
        userRepository.update(user);
    }

    private Cat findCatIfExist(Integer catId) {
        return catRepository.findById(catId);
    }
}