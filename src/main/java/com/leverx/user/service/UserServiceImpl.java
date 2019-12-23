package com.leverx.user.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.user.dto.PointsTransferDto;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.dto.converter.UserDtoConverter;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.validator.UserValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;

import static java.lang.Integer.parseInt;


@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserValidator validator;
    private UserRepository userRepository;
    private UserDtoConverter converter;

    @Override
    public Collection<UserOutputDto> findAll() {
        var users = userRepository.findAll();
        return converter.userCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto findById(int id) throws ElementNotFoundException {
        var optionalUser = userRepository.findById(id);
        var user = optionalUser.orElseThrow(ElementNotFoundException::new);
        return converter.userToUserOutputDto(user);
    }

    @Override
    public Collection<UserOutputDto> findByName(String name) {
        var users = userRepository.findByName(name);
        return converter.userCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto save(UserInputDto userInputDto) throws ValidationFailedException {
        validator.validateCreateUser(userInputDto);
        var user = converter.userInputDtoToUser(userInputDto);
        userRepository.save(user).orElseThrow();
        return converter.userToUserOutputDto(user);
    }

    @Override
    public void deleteById(String id) {
        var parsedId = parseInt(id);
        userRepository.deleteById(parsedId);
    }

    @Override
    public void updateById(String id, UserInputDto userInputDto) throws ValidationFailedException {
        validator.validateUpdateUser(parseInt(id), userInputDto);
        var userId = parseInt(id);
        var user = converter.userInputDtoToUser(userId, userInputDto);
        user.getPets().forEach(pet -> pet.getOwners().add(user));
        userRepository.update(user);
    }

    //TODO: not finished
    @Override
    public void pointsTransfer(String senderIdStr, PointsTransferDto pointsTransferDto) throws ValidationFailedException {
        validator.validatePointsTransfer(senderIdStr, pointsTransferDto);
        var senderId = parseInt(senderIdStr);
        var recipientId = pointsTransferDto.getRecipientId();
        var animalPoints = pointsTransferDto.getAnimalPoints();
        userRepository.pointsTransfer(senderId, recipientId, animalPoints);
    }
}