package com.leverx.validator.message;


import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class ValidationMessageErrors {

    String message;
    Collection<ValidationError> validationErrors = emptyList();
}
