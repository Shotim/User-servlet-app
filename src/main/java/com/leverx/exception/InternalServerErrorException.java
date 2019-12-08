package com.leverx.exception;

public class InternalServerErrorException extends RuntimeException {

    private static final String message = "Internal server error";

    public InternalServerErrorException(Exception e) {
        super(message, e);
    }
}
