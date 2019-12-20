package com.leverx.user.service;

import com.leverx.difactory.Injectable;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.validator.UserValidator;

import java.util.Collection;

import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.user.dto.converter.UserDtoConverter.userCollectionToUserOutputDtoCollection;
import static com.leverx.user.dto.converter.UserDtoConverter.userInputDtoToUser;
import static com.leverx.user.dto.converter.UserDtoConverter.userToUserOutputDto;
import static java.lang.Integer.parseInt;

@Injectable
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = getBean(UserRepository.class);
    private UserValidator validator = new UserValidator();

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
        validator.validateCreateUser(userInputDto);
        var user = userInputDtoToUser(userInputDto);
        userRepository.save(user).orElseThrow();
        return userToUserOutputDto(user);
    }

    @Override
    public void deleteById(String id) {
        int parsedId = parseInt(id);
        userRepository.deleteById(parsedId);
    }

    @Override
    public void updateById(String id, UserInputDto userInputDto) throws ValidationFailedException {
        validator.validateUpdateUser(parseInt(id), userInputDto);
        var userId = parseInt(id);
        var user = userInputDtoToUser(userId, userInputDto);
        user.getPets().forEach(pet -> pet.getOwners().add(user));
        userRepository.update(user);
    }
}