package com.leverx.user.validator;

import com.leverx.cat.validator.CatValidator;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.core.validator.EntityValidator;
import com.leverx.dog.validator.DogValidator;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.core.validator.ValidationErrorMessages.EQUAL_SENDER_AND_RECIPIENT;
import static com.leverx.core.validator.ValidationErrorMessages.NOT_ENOUGH_MONEY;
import static com.leverx.core.validator.ValidationErrorMessages.USER_NOT_FOUND;
import static com.leverx.core.validator.ValidationErrorMessages.getLocalizedMessage;
import static java.util.Collections.emptyList;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@AllArgsConstructor
public class UserValidator {

    private EntityValidator validator;
    private UserRepository userRepository;
    private CatValidator catValidator;
    private DogValidator dogValidator;

    public void validateUpdateUser(int id, UserInputDto userInputDto) throws ValidationFailedException {
        var errorsList = new ArrayList<Optional<String>>(emptyList());
        var userIdErrors = validateUserId(id);
        errorsList.add(userIdErrors);
        var errors = validationUserInputDtoErrors(userInputDto);
        errorsList.addAll(errors);
        String message = createMessageFromList(errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }

    public void validateCreateUser(UserInputDto userInputDto) throws ValidationFailedException {
        var errorsList = new ArrayList<Optional<String>>(emptyList());
        var errors = validationUserInputDtoErrors(userInputDto);
        errorsList.addAll(errors);
        String message = createMessageFromList(errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }

    private String createMessageFromList(ArrayList<Optional<String>> errorsList) {
        return errorsList.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining("; "));
    }

    public void validatePointsTransfer(int senderId, int recipientId, int points) throws ValidationFailedException {
        var errorsList = new ArrayList<Optional<String>>();
        var equalIdsError = validateSenderAndRecipientEqualId(senderId, recipientId);
        errorsList.add(equalIdsError);
        var balanceError = validatePointsBalance(senderId, points);
        errorsList.add(balanceError);
        String message = createMessageFromList(errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message, SC_BAD_REQUEST);
        }
    }

    public Optional<String> validateUserId(int id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            var message = getLocalizedMessage(USER_NOT_FOUND);
            String resultMessage = id + ": " + message;
            return Optional.of(resultMessage);
        }
        return Optional.empty();
    }

    private List<Optional<String>> validationUserInputDtoErrors(UserInputDto userInputDto) {
        var errorsList = new ArrayList<Optional<String>>(emptyList());
        var errors = validator.validate(userInputDto);
        errorsList.add(errors);
        var catsIds = userInputDto.getCatsIds();
        var catsIdsErrors = catValidator.validateCatsIds(catsIds);
        errorsList.add(catsIdsErrors);
        var dogsIds = userInputDto.getDogsIds();
        var dogsIdsErrors = dogValidator.validateDogsIds(dogsIds);
        errorsList.add(dogsIdsErrors);
        return errorsList;
    }

    private Optional<String> validateSenderAndRecipientEqualId(int senderId, int recipientId) {
        if (senderId == recipientId) {
            var message = getLocalizedMessage(EQUAL_SENDER_AND_RECIPIENT);
            return Optional.of(message);
        }
        return Optional.empty();
    }

    private Optional<String> validatePointsBalance(int senderId, int points) {
        var user = userRepository.findById(senderId);
        var animalPointsBalance = user.orElseThrow().getAnimalPoints();
        if (animalPointsBalance < points) {
            var message = getLocalizedMessage(NOT_ENOUGH_MONEY);
            return Optional.of(message);
        }
        return Optional.empty();
    }
}