package com.leverx.entity.cat.validator;

import com.leverx.entity.cat.entity.Cat;
import com.leverx.entity.cat.repository.CatRepository;
import com.leverx.entity.cat.repository.CatRepositoryImpl;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static java.util.Objects.nonNull;

public class CatValidator {

    private static final CatRepository CAT_REPOSITORY = new CatRepositoryImpl();
    private static final String CAT_HAS_OWNER = "Cat with this id already has owner";
    private static final String CAT_DOES_NOT_EXIST = "Cat with this id does not exist in database";

    public static String validateCatsIds(Collection<Integer> catsIds) {

        return catsIds.stream()
                .map(CatValidator::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(";\n"));
    }

    private static Optional<String> getValidationErrors(Integer catId) {
        var optionalCat = CAT_REPOSITORY.findById(catId);
        if (optionalCat.isEmpty()) {
            return Optional.of(getCatNotExistError(catId));
        } else {
            if (hasOwner(optionalCat.get())) {
                return Optional.of(getCatHasOwnerError(catId));
            }
            return Optional.empty();
        }
    }

    private static String getCatHasOwnerError(Integer catId) {
        var invalidValue = catId.toString();
        return invalidValue + ":  " + CAT_HAS_OWNER;
    }

    private static String getCatNotExistError(Integer catId) {
        var invalidValue = catId.toString();
        return invalidValue + ":  " + CAT_DOES_NOT_EXIST;
    }

    private static boolean hasOwner(Cat cat) {
        var owner = cat.getOwner();
        return nonNull(owner);
    }
}