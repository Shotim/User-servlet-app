package com.leverx.core.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.leverx.core.exception.ErrorCode.ELEMENT_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class ElementNotFoundException extends RuntimeException {

    static final String message = "Entity was not found in database";
    @Getter
    static final int errorCode = ELEMENT_NOT_FOUND;
    @Getter
    final int statusCode = SC_NOT_FOUND;

    public ElementNotFoundException() {
        super(message);
    }

    public ElementNotFoundException(String message) {
        super(message);
    }
}
