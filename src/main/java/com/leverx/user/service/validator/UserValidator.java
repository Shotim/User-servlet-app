package com.leverx.user.service.validator;

import com.leverx.user.entity.UserDto;

public class UserValidator {

    private static final int VALID_NAME_LENGTH = 60;

    public static boolean isValidName(UserDto user) {
        return user.getName().length() < VALID_NAME_LENGTH;
    }
}