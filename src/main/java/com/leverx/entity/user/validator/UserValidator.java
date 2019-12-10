package com.leverx.entity.user.validator;

import com.leverx.entity.user.dto.UserInputDto;
import com.leverx.exception.ValidationFailedException;

import static com.leverx.entity.cat.validator.CatValidator.validateCatsIds;
import static com.leverx.validator.EntityValidator.validate;

public class UserValidator {

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