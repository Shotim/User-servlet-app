package com.leverx.validator;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.validator.message.ValidationError;
import com.leverx.validator.message.ValidationErrorsMessage;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
public class EntityValidator {

    private static final CatRepository CAT_REPOSITORY = new CatRepositoryImpl();
    private static final UserRepository USER_REPOSITORY = new UserRepositoryImpl();
    private static final String VALIDATION_FAILED = "Validation failed";
    public static final String NOT_VALID_NAME = "Name should be between 5 and 60 symbols";
    private static final String CAT_HAS_OWNER = "Cat already has owner";
    private static final String CAT_DOES_NOT_EXIST = "Cat does not exist in database";
    private static final String USER_DOES_NOT_EXIST = "User does not exist in database";
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

    public static Optional<ValidationErrorsMessage> validateUserId(Integer id) {
        var optionalUser = USER_REPOSITORY.findById(id);
        if (optionalUser.isEmpty()) {
            ValidationError error = new ValidationError();
            error.setField(id.toString());
            error.setMessage(USER_DOES_NOT_EXIST);

            var errorList = List.of(error);
            var message = new ValidationErrorsMessage(VALIDATION_FAILED);
            message.setValidationErrors(errorList);
            return Optional.of(message);
        }
        return Optional.empty();
    }

    public static Optional<ValidationErrorsMessage> validateCatsIds(Collection<Integer> catsIds, ValidationErrorsMessage message) {
        List<ValidationError> errorList = catsIds.stream()
                .map(EntityValidator::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        if (!errorList.isEmpty()) {
            message = createMessageIfEmpty(message).orElseThrow();
            addErrorsToMessage(message, errorList);
        }
        return Optional.of(message);
    }

    private static void addErrorsToMessage(ValidationErrorsMessage message, List<ValidationError> errorList) {
        if (nonNull(message)) {
            var validationErrors = message.getValidationErrors();
            validationErrors.addAll(errorList);
        }
    }

    private static Optional<ValidationErrorsMessage> createMessageIfEmpty(ValidationErrorsMessage message) {
        if (isNull(message)) {
            message = new ValidationErrorsMessage(VALIDATION_FAILED);
        }
        return Optional.of(message);
    }

    private static Optional<ValidationError> getValidationErrors(Integer catId) {
        var optionalCat = findCatIfExist(catId);
        if (optionalCat.isEmpty()) {
            return Optional.of(getCatNotExistError(catId));
        } else {
            if (hasOwner(optionalCat.get())) {
                return Optional.of(getCatHasOwnerError(catId));
            }
            return Optional.empty();
        }
    }

    private static ValidationError getCatHasOwnerError(Integer catId) {
        var error = new ValidationError();
        error.setMessage(CAT_HAS_OWNER);
        error.setField(catId.toString());
        return error;
    }

    private static ValidationError getCatNotExistError(Integer catId) {
        var error = new ValidationError();
        error.setMessage(CAT_DOES_NOT_EXIST);
        error.setField(catId.toString());
        return error;
    }

    private static boolean hasOwner(Cat cat) {
        var owner = cat.getOwner();
        return nonNull(owner);
    }

    private static Optional<Cat> findCatIfExist(Integer catId) {
        return CAT_REPOSITORY.findById(catId);
    }
}
