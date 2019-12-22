package com.leverx.cat.validator;

import com.leverx.cat.repository.CatRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.validator.ValidationErrorMessages.CAT_DOES_NOT_EXIST;
import static com.leverx.validator.ValidationErrorMessages.getLocalizedMessage;

@AllArgsConstructor
public class CatValidator {

    private CatRepository catRepository;

    public Optional<String> validateCatsIds(Collection<Integer> catsIds) {

        var validationErrors = catsIds.stream()
                .map(this::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining("; "));
        if (validationErrors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(validationErrors);
    }

    private Optional<String> getValidationErrors(Integer catId) {
        var optionalCat = catRepository.findById(catId);
        if (optionalCat.isEmpty()) {
            var message = getLocalizedMessage(CAT_DOES_NOT_EXIST);
            var value = catId.toString() + ": " + message;
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}