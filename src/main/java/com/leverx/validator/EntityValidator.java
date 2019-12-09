package com.leverx.validator;

import com.leverx.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Validator;

import static java.util.stream.Collectors.joining;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
public class EntityValidator {

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 60;
    public static final String NOT_VALID_NAME = "Name should be between " + MIN_SIZE + " and " + MAX_SIZE + " symbols";
    public static final String NOT_VALID_DATE = "Must be a date in the past or in the present";
    private static Validator validator = buildDefaultValidatorFactory().getValidator();

    public static <T> void validateEntity(T entity) throws ValidationFailedException {
        String errors = validate(entity);
        if (!errors.isEmpty()) {
            throw new ValidationFailedException(errors);
        }
    }

    public static <T> String validate(T entity) {
        var violations = validator.validate(entity);
        var violationAmount = violations.size();

        if (violationAmount == 0) {
            return "";
        }

        return violations.stream()
                .map(violation -> {
                    var invalidValue = violation.getInvalidValue().toString();
                    var invalidCause = violation.getMessage();
                    return invalidValue + ":  " + invalidCause;
                })
                .collect(joining("; "));
    }
}
