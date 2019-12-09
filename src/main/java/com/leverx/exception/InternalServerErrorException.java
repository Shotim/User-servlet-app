package com.leverx.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class InternalServerErrorException extends RuntimeException {

    static final String message = "Internal server error";

    @Getter
    final int statusCode = SC_INTERNAL_SERVER_ERROR;

    @Getter
    static final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(Exception e) {
        super(message, e);
    }
}
