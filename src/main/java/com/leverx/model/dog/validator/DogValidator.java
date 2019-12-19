package com.leverx.model.dog.validator;

import com.leverx.model.dog.repository.DogRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.difactory.DIFactory.getBean;

public class DogValidator {

    private static final DogRepository DOG_REPOSITORY =
            (DogRepository) getBean(DogRepository.class);
    private static final String DOG_DOES_NOT_EXIST = "Dog with this id does not exist in database";

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
            return Optional.of(getDogNotExistError(dogId));
        } else {
            return Optional.empty();
        }
    }

    private static String getDogNotExistError(Integer dogId) {
        var invalidValue = dogId.toString();
        return invalidValue + ":  " + DOG_DOES_NOT_EXIST;
    }
}
