package com.leverx.user.validator;

import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.validator.message.ValidationError;
import com.leverx.validator.message.ValidationErrorsMessage;

import java.util.List;
import java.util.Optional;

public class UserValidator {

    private static final String VALIDATION_FAILED = "Validation failed";
    private static final UserRepository USER_REPOSITORY = new UserRepositoryImpl();
    private static final String USER_DOES_NOT_EXIST = "User does not exist in database";

    public static Optional<ValidationErrorsMessage> validateUserId(Integer id) {
        var optionalUser = USER_REPOSITORY.findById(id);
        if (optionalUser.isEmpty()) {
            var error = new ValidationError();
            error.setField(id.toString());
            error.setMessage(USER_DOES_NOT_EXIST);

            var errorList = List.of(error);
            var message = new ValidationErrorsMessage(VALIDATION_FAILED);
            message.setValidationErrors(errorList);
            return Optional.of(message);
        }
        return Optional.empty();
    }
}
