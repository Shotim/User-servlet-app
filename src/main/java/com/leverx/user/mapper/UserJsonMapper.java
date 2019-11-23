package com.leverx.user.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class UserJsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String convertFromUserToJson(User user) {
        try {
            return OBJECT_MAPPER.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public static Collection<String> convertFromUserCollectionToJson(Collection<User> users) {
        return users.stream()
                .map(UserJsonMapper::convertFromUserToJson)
                .collect(toList());
    }

    public static UserDto convertFromJsonToUserDto(String user) {
        try {
            return OBJECT_MAPPER.readValue(user, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
