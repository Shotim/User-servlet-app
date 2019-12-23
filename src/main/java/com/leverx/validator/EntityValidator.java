package com.leverx.validator;

import com.leverx.core.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
public class EntityValidator {

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 60;

    public <T> void validateEntity(T entity) throws ValidationFailedException {
        var errors = validate(entity);
        if (errors.isPresent()) {
            throw new ValidationFailedException(errors.get());
        }
    }

    public <T> Optional<String> validate(T entity) {
        var validator = buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(entity);
        var violationAmount = violations.size();

        if (violationAmount == 0) {
            return Optional.empty();
        }

        var message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(joining("; "));
        return Optional.of(message);
    }
}
