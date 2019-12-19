package com.leverx.model.cat.validator;

import com.leverx.model.cat.repository.CatRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.difactory.DIFactory.getBean;

public class CatValidator {

    private static final CatRepository CAT_REPOSITORY =
            (CatRepository) getBean(CatRepository.class);
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
            return Optional.empty();
        }
    }

    private static String getCatNotExistError(Integer catId) {
        var invalidValue = catId.toString();
        return invalidValue + ":  " + CAT_DOES_NOT_EXIST;
    }
}