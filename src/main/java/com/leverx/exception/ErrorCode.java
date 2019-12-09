package com.leverx.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public enum ErrorCode {

    VALIDATION_FAILED(7135), ELEMENT_NOT_FOUND(4020), INTERNAL_SERVER_ERROR(343);

    @Getter
    int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
