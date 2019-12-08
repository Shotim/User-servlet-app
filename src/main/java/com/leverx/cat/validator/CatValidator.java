package com.leverx.cat.validator;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.validator.message.ValidationError;
import com.leverx.validator.message.ValidationErrorsMessage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class CatValidator {

    private static final CatRepository CAT_REPOSITORY = new CatRepositoryImpl();
    private static final String CAT_HAS_OWNER = "Cat already has owner";
    private static final String CAT_DOES_NOT_EXIST = "Cat does not exist in database";
    private static final String VALIDATION_FAILED = "Validation failed";

    public static Optional<ValidationErrorsMessage> validateCatsIds(Collection<Integer> catsIds, ValidationErrorsMessage message) {
        var errorList = catsIds.stream()
                .map(CatValidator::getValidationErrors)
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
