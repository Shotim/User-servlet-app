package com.leverx.user.service;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.entity.User;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.leverx.user.dto.UserDtoConverter.convertUserCollectionToUserOutputDtoCollection;
import static com.leverx.user.dto.UserDtoConverter.convertUserInputDtoToUser;
import static com.leverx.user.dto.UserDtoConverter.convertUserToUserOutputDto;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public Collection<UserOutputDto> findAll() {
        Collection<User> users = userRepository.findAll();
        return convertUserCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto findById(int id) throws NoSuchElementException {
        var optionalUser = userRepository.findById(id);
        var user = optionalUser.orElseThrow();
        return convertUserToUserOutputDto(user);
    }

    @Override
    public Collection<UserOutputDto> findByName(String name) {
        var users = userRepository.findByName(name);
        return convertUserCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto save(UserInputDto userInputDto) {
        User user = convertUserInputDtoToUser(userInputDto);
        userRepository.save(user);
        return convertUserToUserOutputDto(user);
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
    public UserOutputDto updateById(String id, UserInputDto userInputDto) {
        var userId = parseInt(id);
        User user = convertUserInputDtoToUser(userId, userInputDto);
        isValid(userInputDto);
        userRepository.update(user);
        return convertUserToUserOutputDto(user);
    }
}