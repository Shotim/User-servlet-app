package com.leverx.validator.message;


import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class ValidationErrorsMessage {

    @NonNull
    String message;
    Collection<ValidationError> validationErrors = new ArrayList<>();
}
