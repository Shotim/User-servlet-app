package com.leverx.core.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.leverx.core.exception.ErrorCode.VALIDATION_FAILED;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class ValidationFailedException extends RuntimeException {

    public static final int SC_UNPROCESSABLE_ENTITY = 422;
    @Getter
    static final int errorCode = VALIDATION_FAILED;
    @Getter
    final int statusCode = SC_UNPROCESSABLE_ENTITY;

    public ValidationFailedException(String errorMessage) {
        super(errorMessage);
    }
}
