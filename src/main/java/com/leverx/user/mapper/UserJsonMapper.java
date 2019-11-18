package com.leverx.user.mapper;

import com.google.gson.Gson;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class UserJsonMapper {

    private static final Gson gson = new Gson();

    public static String convertToJson(User user) {
        return gson.toJson(user);
    }

    public static Collection<String> convertToJson(Collection<User> users) {
        return users.stream()
                .map(UserJsonMapper::convertToJson)
                .collect(toList());
    }

    public static UserDto convertFromJsonToUserDto(String user) {
        return gson.fromJson(user, UserDto.class);
    }
}
