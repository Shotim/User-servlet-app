package com.leverx.user.validator;

import com.leverx.exception.ValidationFailedException;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.validator.EntityValidator;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.bundle.BundleConstants.USER_NOT_FOUND;
import static com.leverx.bundle.BundleConstants.getLocalizedMessage;
import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.cat.validator.CatValidator.validateCatsIds;
import static com.leverx.dog.validator.DogValidator.validateDogsIds;
import static java.lang.String.join;
import static java.util.Collections.emptyList;

public class UserValidator {

    private UserRepository userRepository = getBean(UserRepository.class);
    private EntityValidator validator = new EntityValidator();

    public void validateUpdateUser(int id, UserInputDto userInputDto) throws ValidationFailedException {
        List<String> errorsList = new ArrayList<>(emptyList());
        var userIdErrors = validateUserId(id);
        errorsList.add(userIdErrors);
        List<String> errors = validationUserInputDtoErrors(userInputDto);
        errorsList.addAll(errors);
        String message = join("; ", errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }

    public void validateCreateUser(UserInputDto userInputDto) throws ValidationFailedException {
        List<String> errorsList = new ArrayList<>(emptyList());
        List<String> errors = validationUserInputDtoErrors(userInputDto);
        errorsList.addAll(errors);
        String message = join("; ", errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }

    public String validateUserId(int id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            var message = getLocalizedMessage(USER_NOT_FOUND);
            return id + ": " + message;
        }
        return "";
    }

    private List<String> validationUserInputDtoErrors(UserInputDto userInputDto) {
        List<String> errorsList = new ArrayList<>(emptyList());
        var errors = validator.validate(userInputDto);
        errorsList.add(errors);
        var catsIds = userInputDto.getCatsIds();
        var catsIdsErrors = validateCatsIds(catsIds);
        errorsList.add(catsIdsErrors);
        var dogsIds = userInputDto.getDogsIds();
        var dogsIdsErrors = validateDogsIds(dogsIds);
        errorsList.add(dogsIdsErrors);
        return errorsList;
    }
}