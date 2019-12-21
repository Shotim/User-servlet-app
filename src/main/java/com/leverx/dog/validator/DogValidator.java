package com.leverx.dog.validator;

import com.leverx.dog.repository.DogRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.bundle.BundleConstants.DOG_DOES_NOT_EXIST;
import static com.leverx.bundle.BundleConstants.getLocalizedMessage;
import static com.leverx.difactory.DIFactory.getBean;

public class DogValidator {

    private static final DogRepository DOG_REPOSITORY = getBean(DogRepository.class);

    public static Optional<String> validateDogsIds(Collection<Integer> dogsIds) {

        String validationErrors = dogsIds.stream()
                .map(DogValidator::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(";\n"));
        return Optional.of(validationErrors);
    }

    private static Optional<String> getValidationErrors(Integer dogId) {
        var optionalDog = DOG_REPOSITORY.findById(dogId);
        if (optionalDog.isEmpty()) {
            var message = getLocalizedMessage(DOG_DOES_NOT_EXIST);
            var value = dogId.toString() + ": " + message;
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
