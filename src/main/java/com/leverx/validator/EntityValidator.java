package com.leverx.validator;

import com.leverx.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static java.util.stream.Collectors.joining;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
public class EntityValidator {

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 60;
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
                .map(ConstraintViolation::getMessage)
                .collect(joining("; "));
    }
}
