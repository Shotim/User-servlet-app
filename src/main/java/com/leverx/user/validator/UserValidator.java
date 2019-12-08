package com.leverx.user.validator;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.validator.ValidationFailedException;

import static com.leverx.cat.validator.CatValidator.validateCatsIds;
import static com.leverx.validator.EntityValidator.validate;

public class UserValidator {

    private static final UserRepository USER_REPOSITORY = new UserRepositoryImpl();
    private static final String USER_DOES_NOT_EXIST = "User does not exist in database";

    public static String validateUserId(Integer id) {
        var optionalUser = USER_REPOSITORY.findById(id);
        if (optionalUser.isEmpty()) {
            var invalidValue = id.toString();
            return invalidValue + ":  " + USER_DOES_NOT_EXIST;
        }
        return "";
    }

    public static void validateUserInputDto(UserInputDto userInputDto) throws ValidationFailedException {
        var errors = validate(userInputDto);
        var catsIds = userInputDto.getCatsIds();
        var idsErrors = validateCatsIds(catsIds);
        if (!errors.isEmpty() || !idsErrors.isEmpty()) {
            String message = errors + idsErrors;
            throw new ValidationFailedException(message);
        }
    }
}