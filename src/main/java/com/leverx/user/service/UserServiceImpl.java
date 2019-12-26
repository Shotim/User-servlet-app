package com.leverx.user.service;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.dto.converter.UserDtoConverter;
import com.leverx.user.entity.User;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.validator.UserValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.leverx.validator.ValidationErrorMessages.USER_NOT_FOUND;
import static com.leverx.validator.ValidationErrorMessages.getLocalizedMessage;
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
    public UserOutputDto findById(int id) {
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
    public UserOutputDto save(UserInputDto userInputDto) {
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
    public void updateById(String id, UserInputDto userInputDto) {
        validator.validateUpdateUser(parseInt(id), userInputDto);
        var userId = parseInt(id);
        var user = converter.userInputDtoToUser(userId, userInputDto);
        user.getPets().forEach(pet -> pet.getOwners().add(user));
        userRepository.update(user);
    }

    @Override
    public void pointsTransfer(String senderIdStr, String recipientIdStr, String pointsStr) {
        var senderId = parseInt(senderIdStr);
        var recipientId = parseInt(recipientIdStr);
        int points = parseInt(pointsStr);
        var sender = getUserById(senderId);
        var recipient = getUserById(recipientId);
        validator.validatePointsTransfer(senderIdStr, recipientIdStr, pointsStr);
        removePointsFromSender(points, sender);
        addPointsToRecipient(points, recipient);
        userRepository.update(sender);
        userRepository.update(recipient);
    }

    public User getUserById(int id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            var message = getLocalizedMessage(USER_NOT_FOUND);
            throw new ValidationFailedException(id + ": " + message);
        }
    }

    private void addPointsToRecipient(int points, User recipient) {
        var recipientBalance = recipient.getAnimalPoints();
        recipient.setAnimalPoints(recipientBalance + points);
    }

    private void removePointsFromSender(int points, User sender) {
        var senderBalance = sender.getAnimalPoints();
        sender.setAnimalPoints(senderBalance - points);
    }
}