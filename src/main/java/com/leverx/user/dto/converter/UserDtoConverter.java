package com.leverx.user.dto.converter;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.entity.User;

import java.util.Collection;

import static com.leverx.cat.dto.converter.CatDtoConverter.convertCatCollectionToCatOutputDtoCollection;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserDtoConverter {

    public static User convertUserInputDtoToUser(int id, UserInputDto userInputDto) {
        var name = userInputDto.getName();
        return new User(id, name);
    }

    public static User convertUserInputDtoToUser(UserInputDto userInputDto) {
        int DEFAULT_USER_ID = 0;
        return convertUserInputDtoToUser(DEFAULT_USER_ID, userInputDto);
    }

    public static UserOutputDto convertUserToUserOutputDto(User user) {
        var id = user.getId();
        var name = user.getName();
        var cats = nonNull(user.getCats()) ? convertCatCollectionToCatOutputDtoCollection(user.getCats()) : null;
        return new UserOutputDto(id, name, cats);
    }

    public static Collection<UserOutputDto> convertUserCollectionToUserOutputDtoCollection(Collection<User> users) {
        return users.stream()
                .map(UserDtoConverter::convertUserToUserOutputDto)
                .collect(toList());
    }
}
