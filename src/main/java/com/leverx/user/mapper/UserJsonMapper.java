package com.leverx.user.mapper;

import com.google.gson.Gson;
import com.leverx.user.entity.DTOUser;
import com.leverx.user.entity.User;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class UserJsonMapper {

    private static final Gson gson = new Gson();

    public static String convertToJson(User user) {
        return gson.toJson(user);
    }

    public static Collection<String> convertToJson(Collection<User> users) {
        return users.stream()
                .map(gson::toJson)
                .collect(toList());
    }

    public static DTOUser convertFromJsonToDTOUser(String user) {
        return gson.fromJson(user,DTOUser.class);
    }
}
