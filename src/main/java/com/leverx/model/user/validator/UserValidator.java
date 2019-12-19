package com.leverx.model.user.validator;

import com.leverx.exception.ValidationFailedException;
import com.leverx.model.user.dto.UserInputDto;
import com.leverx.model.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.model.cat.validator.CatValidator.validateCatsIds;
import static com.leverx.model.dog.validator.DogValidator.validateDogsIds;
import static com.leverx.validator.EntityValidator.validate;
import static java.lang.String.join;
import static java.util.Collections.emptyList;

public class UserValidator {

    private static UserRepository userRepository = (UserRepository)
            getBean(UserRepository.class);
    private static final String USER_NOT_FOUND = "User with this id was not found";

    public static void validateUpdateUser(int id, UserInputDto userInputDto) throws ValidationFailedException {
        List<String> errorsList = new ArrayList<>(emptyList());
        var userIdErrors = validateUserId(id);
        errorsList.add(userIdErrors);
        List<String> errors = validationUserInputDtoErrors(userInputDto);
        errorsList.addAll(errors);
        String message = join("", errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }

    public static String validateUserId(int id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            return USER_NOT_FOUND;
        }
        return "";
    }

    public static void validateCreateUser(UserInputDto userInputDto) throws ValidationFailedException {
        List<String> errorsList = new ArrayList<>(emptyList());
        List<String> errors = validationUserInputDtoErrors(userInputDto);
        errorsList.addAll(errors);
        String message = join("", errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }

    private static List<String> validationUserInputDtoErrors(UserInputDto userInputDto) {
        List<String> errorsList = new ArrayList<>(emptyList());
        var errors = validate(userInputDto);
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