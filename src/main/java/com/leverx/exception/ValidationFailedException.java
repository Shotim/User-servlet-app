package com.leverx.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class ValidationFailedException extends Exception {

    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    @Getter
    final int statusCode = SC_UNPROCESSABLE_ENTITY;

    @Getter
    static final ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    public ValidationFailedException(String errorMessage) {
        super(errorMessage);
    }
}
