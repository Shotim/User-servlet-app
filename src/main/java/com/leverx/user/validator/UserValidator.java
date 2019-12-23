package com.leverx.user.validator;

import com.leverx.cat.validator.CatValidator;
import com.leverx.dog.validator.DogValidator;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.user.dto.PointsTransferDto;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.validator.EntityValidator;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.validator.ValidationErrorMessages.EQUAL_SENDER_AND_RECIPIENT;
import static com.leverx.validator.ValidationErrorMessages.NOT_ENOUGH_MONEY;
import static com.leverx.validator.ValidationErrorMessages.USER_NOT_FOUND;
import static com.leverx.validator.ValidationErrorMessages.getLocalizedMessage;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;

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

    public void validatePointsTransfer(String senderId, PointsTransferDto pointsTransferDto) throws ValidationFailedException {
        var errorsList = new ArrayList<Optional<String>>();
        var errors = validator.validate(pointsTransferDto);
        errorsList.add(errors);
        var senderExistence = validateSenderExistence(senderId);
        errorsList.add(senderExistence);
        var recipientExistence = validateRecipientExistence(pointsTransferDto);
        errorsList.add(recipientExistence);
        var equalIdsError = validateSenderAndRecipientEqualId(senderId, pointsTransferDto);
        errorsList.add(equalIdsError);
        if (recipientExistence.isPresent()) {
            var balanceError = validatePointsBalance(senderId, pointsTransferDto);
            errorsList.add(balanceError);
        }
        String message = createMessageFromList(errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
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

    private Optional<String> validateSenderAndRecipientEqualId(String id, PointsTransferDto pointsTransferDto) {
        if (parseInt(id) == pointsTransferDto.getRecipientId()) {
            var message = getLocalizedMessage(EQUAL_SENDER_AND_RECIPIENT);
            return Optional.of(message);
        }
        return Optional.empty();
    }

    private Optional<String> validatePointsBalance(String senderId, PointsTransferDto pointsTransferDto) {
        var user = userRepository.findById(parseInt(senderId));
        var animalPointsBalance = user.orElseThrow().getAnimalPoints();
        if (animalPointsBalance < pointsTransferDto.getAnimalPoints()) {
            var message = getLocalizedMessage(NOT_ENOUGH_MONEY);
            return Optional.of(message);
        }
        return Optional.empty();
    }

    private Optional<String> validateRecipientExistence(PointsTransferDto pointsTransferDto) {
        var recipientId = pointsTransferDto.getRecipientId();
        return validateUserId(recipientId);
    }

    private Optional<String> validateSenderExistence(String senderId) {
        return validateUserId(parseInt(senderId));
    }
}