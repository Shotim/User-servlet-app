package com.leverx.user.service;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.dto.converter.UserDtoConverter;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.validator.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.leverx.user.dto.converter.UserDtoConverter.userCollectionToUserOutputDtoCollection;
import static com.leverx.user.dto.converter.UserDtoConverter.userInputDtoToUser;
import static com.leverx.user.dto.converter.UserDtoConverter.userToUserOutputDto;
import static com.leverx.user.validator.UserValidator.validateUserInputDto;
import static java.lang.Integer.parseInt;

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
    public UserOutputDto save(UserInputDto userInputDto) throws ValidationFailedException {
        validateUserInputDto(userInputDto);
        var user = userInputDtoToUser(userInputDto);
        user.getCats().forEach(cat -> cat.setOwner(user));
        userRepository.save(user);
        return userToUserOutputDto(user);
    }

    @Override
    public void deleteById(String id) {
        int parsedId = parseInt(id);
        userRepository.deleteById(parsedId);
    }

    @Override
    public UserOutputDto updateById(String id, UserInputDto userInputDto) throws ValidationFailedException {
        validateUserInputDto(userInputDto);
        var userId = parseInt(id);
        var user = UserDtoConverter.userInputDtoToUser(userId, userInputDto);
        user.getCats().forEach(cat -> cat.setOwner(user));
        userRepository.update(user);
        return userToUserOutputDto(user);
    }
}