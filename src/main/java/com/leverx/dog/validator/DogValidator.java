package com.leverx.dog.validator;

import com.leverx.dog.repository.DogRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.core.validator.ValidationErrorMessages.DOG_DOES_NOT_EXIST;
import static com.leverx.core.validator.ValidationErrorMessages.getLocalizedMessage;

@AllArgsConstructor
public class DogValidator {

    private DogRepository dogRepository;

    public Optional<String> validateDogsIds(Collection<Integer> dogsIds) {

        var validationErrors = dogsIds.stream()
                .map(this::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining("; "));
        if (validationErrors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(validationErrors);
    }

    private Optional<String> getValidationErrors(Integer dogId) {
        var optionalDog = dogRepository.findById(dogId);
        if (optionalDog.isEmpty()) {
            var message = getLocalizedMessage(DOG_DOES_NOT_EXIST);
            var value = dogId.toString() + ": " + message;
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
