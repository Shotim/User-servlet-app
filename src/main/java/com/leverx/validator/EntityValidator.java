package com.leverx.validator;

import com.leverx.validator.message.ValidationError;
import com.leverx.validator.message.ValidationErrorsMessage;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
public class EntityValidator {

    public static final String NOT_VALID_NAME = "Name should be between 5 and 60 symbols";
    private static final String VALIDATION_FAILED = "Validation failed";
    private static Validator validator = buildDefaultValidatorFactory().getValidator();

    public static <T> Optional<ValidationErrorsMessage> isValid(T entity) {
        var violations = validator.validate(entity);
        var violationAmount = violations.size();

        if (violationAmount == 0) {
            return Optional.empty();
        }

        ValidationErrorsMessage message = new ValidationErrorsMessage(VALIDATION_FAILED);

        List<ValidationError> validationErrors = violations.stream()
                .map(violation -> {
                    var error = new ValidationError();
                    error.setField(violation.getInvalidValue().toString());
                    error.setMessage(violation.getMessage());
                    return error;
                }).collect(toList());
        message.setValidationErrors(validationErrors);
        return Optional.of(message);
    }
}
