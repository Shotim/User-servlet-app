package com.leverx.validator;

import org.slf4j.Logger;

import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.slf4j.LoggerFactory.getLogger;

public class EntityValidator {

    public static final String NOT_VALID_NAME = "Name should be between 5 and 60 symbols";
    private static final Logger LOGGER = getLogger(EntityValidator.class);
    private static Validator validator = buildDefaultValidatorFactory().getValidator();

    public static <T> boolean isValid(T entity) {
        var violations = validator.validate(entity);
        var violationAmount = violations.size();

        if (violationAmount == 0) {
            return true;
        }
        violations
                .forEach(violation -> LOGGER.error(violation.getMessage()));
        return false;
    }
}
