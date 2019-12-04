package com.leverx.validator.message;


import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class ValidationError {

    String field;
    String message;
}
