package com.leverx.core.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.leverx.core.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class InternalServerErrorException extends RuntimeException {

    @Getter
    static final int errorCode = INTERNAL_SERVER_ERROR;
    @Getter
    final int statusCode = SC_INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(String message) {
        super(message);
    }
}
