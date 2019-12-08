package com.leverx.user.service;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.dto.converter.UserDtoConverter;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.leverx.user.dto.converter.UserDtoConverter.userCollectionToUserOutputDtoCollection;
import static com.leverx.user.dto.converter.UserDtoConverter.userInputDtoToUser;
import static com.leverx.user.dto.converter.UserDtoConverter.userToUserOutputDto;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public Collection<UserOutputDto> findAll() {
        var users = userRepository.findAll();
        return userCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto findById(int id) throws NoSuchElementException {
        var optionalUser = userRepository.findById(id);
        var user = optionalUser.orElseThrow();
        return userToUserOutputDto(user);
    }

    @Override
    public Collection<UserOutputDto> findByName(String name) {
        var users = userRepository.findByName(name);
        return userCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto save(UserInputDto userInputDto) {
        var user = userInputDtoToUser(userInputDto);
        userRepository.save(user);
        return userToUserOutputDto(user);
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
        var user = UserDtoConverter.userInputDtoToUser(userId, userInputDto);
        isValid(userInputDto);
        userRepository.update(user);
        return userToUserOutputDto(user);
    }
}