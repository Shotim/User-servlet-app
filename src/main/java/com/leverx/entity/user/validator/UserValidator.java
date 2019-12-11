package com.leverx.entity.user.validator;

import com.leverx.entity.user.dto.UserInputDto;
import com.leverx.exception.ValidationFailedException;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.entity.cat.validator.CatValidator.validateCatsIds;
import static com.leverx.entity.dog.validator.DogValidator.validateDogsIds;
import static com.leverx.validator.EntityValidator.validate;
import static java.lang.String.join;
import static java.util.Collections.emptyList;

public class UserValidator {

    public static void validateUserInputDto(UserInputDto userInputDto) throws ValidationFailedException {
        List<String> errorsList = new ArrayList<>(emptyList());
        var errors = validate(userInputDto);
        errorsList.add(errors);
        var catsIds = userInputDto.getCatsIds();
        var catsIdsErrors = validateCatsIds(catsIds);
        errorsList.add(catsIdsErrors);
        var dogsIds = userInputDto.getDogsIds();
        var dogsIdsErrors = validateDogsIds(dogsIds);
        errorsList.add(dogsIdsErrors);
        String message = join("", errorsList);
        if (!message.isEmpty()) {
            throw new ValidationFailedException(message);
        }
    }
}