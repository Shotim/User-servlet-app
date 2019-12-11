package com.leverx.entity.user.service;

import com.leverx.entity.user.dto.UserInputDto;
import com.leverx.entity.user.dto.UserOutputDto;
import com.leverx.entity.user.repository.UserRepository;
import com.leverx.entity.user.repository.UserRepositoryImpl;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static com.leverx.entity.user.dto.converter.UserDtoConverter.userCollectionToUserOutputDtoCollection;
import static com.leverx.entity.user.dto.converter.UserDtoConverter.userInputDtoToUser;
import static com.leverx.entity.user.dto.converter.UserDtoConverter.userToUserOutputDto;
import static com.leverx.entity.user.validator.UserValidator.validateUserInputDto;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;

@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public Collection<UserOutputDto> findAll() {
        var users = userRepository.findAll();
        return userCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto findById(int id) throws ElementNotFoundException {
        var optionalUser = userRepository.findById(id);
        var user = optionalUser.orElseThrow(ElementNotFoundException::new);
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
        var pets = user.getPets();
        user.setPets(emptyList());
        userRepository.save(user).orElseThrow();
        user.setPets(pets);
        user.getPets().forEach(pet -> pet.setOwner(user));
        userRepository.update(user);
        return userToUserOutputDto(user);
    }

    @Override
    public void deleteById(String id) {
        int parsedId = parseInt(id);
        userRepository.deleteById(parsedId);
    }

    @Override
    public void updateById(String id, UserInputDto userInputDto) throws ValidationFailedException {
        validateUserInputDto(userInputDto);
        var userId = parseInt(id);
        var user = userInputDtoToUser(userId, userInputDto);
        user.getPets().forEach(pet -> pet.setOwner(user));
        userRepository.update(user);
    }
}