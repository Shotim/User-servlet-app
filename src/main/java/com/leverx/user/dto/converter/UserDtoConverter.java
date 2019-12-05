package com.leverx.user.dto.converter;

import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.entity.User;

import java.util.Collection;
import java.util.Optional;

import static com.leverx.cat.dto.converter.CatDtoConverter.convertCatCollectionToCatOutputDtoCollection;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserDtoConverter {

    private static final CatRepository catRepository = new CatRepositoryImpl();

    public static User convertUserInputDtoToUser(int id, UserInputDto userInputDto) {
        var name = userInputDto.getName();
        var cats = userInputDto.getCatsIdsList().stream()
                .map(catRepository::findById)
                .map(Optional::orElseThrow)
                .collect(toList());
        return new User(id, name, cats);
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
