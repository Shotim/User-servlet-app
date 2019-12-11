package com.leverx.entity.dog.validator;

import com.leverx.entity.dog.entity.Dog;
import com.leverx.entity.dog.repository.DogRepository;
import com.leverx.entity.dog.repository.DogRepositoryImpl;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static java.util.Objects.nonNull;

public class DogValidator {

    private static final DogRepository DOG_REPOSITORY = new DogRepositoryImpl();
    private static final String DOG_HAS_OWNER = "Dog with this id already has owner";
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
            if (hasOwner(optionalDog.get())) {
                return Optional.of(getDogHasOwnerError(dogId));
            }
            return Optional.empty();
        }
    }

    private static String getDogHasOwnerError(Integer dogId) {
        var invalidValue = dogId.toString();
        return invalidValue + ":  " + DOG_HAS_OWNER;
    }

    private static String getDogNotExistError(Integer dogId) {
        var invalidValue = dogId.toString();
        return invalidValue + ":  " + DOG_DOES_NOT_EXIST;
    }

    private static boolean hasOwner(Dog dog) {
        var owner = dog.getOwner();
        return nonNull(owner);
    }
}
