package com.leverx.model.dog.validator;

import com.leverx.model.dog.repository.DogRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.bundle.BundleConstants.DOG_DOES_NOT_EXIST;
import static com.leverx.bundle.BundleConstants.getLocalizedMessage;
import static com.leverx.difactory.DIFactory.getBean;

public class DogValidator {

    private static final DogRepository DOG_REPOSITORY = getBean(DogRepository.class);

    public static String validateDogsIds(Collection<Integer> dogsIds) {

        return dogsIds.stream()
                .map(DogValidator::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(";\n"));
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
