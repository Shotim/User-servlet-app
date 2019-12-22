package com.leverx.cat.validator;

import com.leverx.cat.repository.CatRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.bundle.BundleConstants.CAT_DOES_NOT_EXIST;
import static com.leverx.bundle.BundleConstants.getLocalizedMessage;
import static com.leverx.difactory.DIFactory.getBean;

public class CatValidator {

    private static final CatRepository CAT_REPOSITORY = getBean(CatRepository.class);

    public static Optional<String> validateCatsIds(Collection<Integer> catsIds) {

        var validationErrors = catsIds.stream()
                .map(CatValidator::getValidationErrors)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining("; "));
        if (validationErrors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(validationErrors);
    }

    private static Optional<String> getValidationErrors(Integer catId) {
        var optionalCat = CAT_REPOSITORY.findById(catId);
        if (optionalCat.isEmpty()) {
            var message = getLocalizedMessage(CAT_DOES_NOT_EXIST);
            var value = catId.toString() + ": " + message;
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}