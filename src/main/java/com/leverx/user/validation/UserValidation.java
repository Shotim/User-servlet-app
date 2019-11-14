package com.leverx.user.entity;

public class UserValidation {

    public static final int VALID_NAME_LENGTH = 60;

    public static boolean isValidName(DTOUser user) {
        return user.getName().length() < VALID_NAME_LENGTH;
    }
}
