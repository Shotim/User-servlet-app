package com.leverx.user.servlet.validation;

import com.leverx.user.entity.UserDto;

public class UserValidation {

    public static final int VALID_NAME_LENGTH = 60;

    public static boolean isValidName(UserDto user) {
        return user.getName().length() < VALID_NAME_LENGTH;
    }
}