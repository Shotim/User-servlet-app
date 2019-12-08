package com.leverx.validator;

public class ValidationFailedException extends Exception {

    public ValidationFailedException(String errorMessage) {
        super(errorMessage);
    }
}
