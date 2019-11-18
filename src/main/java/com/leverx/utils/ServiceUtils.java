package com.leverx.utils;

import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

public class ServiceUtils {

    public static User convertUserDtoToUser(int id, UserDto userDto) {
        var name = userDto.getName();
        return new User(id, name);
    }

    public static User convertUserDtoToUser(UserDto userDto) {
        int DEFAULT_USER_ID = 0;
        return convertUserDtoToUser(DEFAULT_USER_ID, userDto);
    }
}
