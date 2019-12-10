package com.leverx.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.leverx.exception.ErrorCode.ELEMENT_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class ElementNotFoundException extends Exception {

    static final String message = "Entity was not found in database";

    @Getter
    final int statusCode = SC_NOT_FOUND;

    @Getter
    static final int errorCode = ELEMENT_NOT_FOUND;

    public ElementNotFoundException() {
        super(message);
    }
}
