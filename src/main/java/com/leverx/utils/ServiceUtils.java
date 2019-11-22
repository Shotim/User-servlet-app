package com.leverx.utils;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatDto;
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

    public static Cat convertCatDtoToCat(CatDto catDto){
        var name = catDto.getName();
        return new Cat(name);
    }
}