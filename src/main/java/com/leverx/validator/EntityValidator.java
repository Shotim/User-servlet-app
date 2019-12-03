package com.leverx.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
public class EntityValidator {

    public static final String NOT_VALID_NAME = "Name should be between 5 and 60 symbols";
    private static Validator validator = buildDefaultValidatorFactory().getValidator();

    public static <T> boolean isValid(T entity) {
        var violations = validator.validate(entity);
        var violationAmount = violations.size();

        if (violationAmount == 0) {
            return true;
        }
        violations
                .forEach(violation -> log.error(violation.getMessage()));
        return false;
    }
}
