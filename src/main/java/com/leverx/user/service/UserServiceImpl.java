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
import org.hibernate.TransactionException;

import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import java.util.Collection;
import java.util.NoSuchElementException;

import static com.leverx.core.config.HibernateEMFConfig.getEntityManager;
import static com.leverx.core.utils.RepositoryUtils.beginTransaction;
import static com.leverx.core.utils.RepositoryUtils.rollbackTransactionIfActive;
import static com.leverx.core.validator.ValidationErrorMessages.USER_NOT_FOUND;
import static com.leverx.core.validator.ValidationErrorMessages.getLocalizedMessage;
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
    public void pointsTransfer(int senderId, int recipientId, int points) {
        var entityManager = getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction(entityManager);
            var sender = getUserById(senderId);
            var recipient = getUserById(recipientId);
            validator.validatePointsTransfer(senderId, recipientId, points);
            removePointsFromUser(points, sender);
            addPointsToUser(points, recipient);
            userRepository.update(sender);
            userRepository.update(recipient);
            transaction.commit();
        } catch (RollbackException e) {
            rollbackTransactionIfActive(transaction);
            throw new TransactionException(e.getMessage());
        }
    }

    public User getUserById(int id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            var message = getLocalizedMessage(USER_NOT_FOUND);
            throw new ValidationFailedException(id + ": " + message);
        }
    }

    private void addPointsToUser(int points, User recipient) {
        var recipientBalance = recipient.getAnimalPoints();
        recipient.setAnimalPoints(recipientBalance + points);
    }

    private void removePointsFromUser(int points, User sender) {
        var senderBalance = sender.getAnimalPoints();
        sender.setAnimalPoints(senderBalance - points);
    }
}