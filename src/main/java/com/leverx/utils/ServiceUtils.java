package com.leverx.utils;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.user.entity.User;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;

import java.util.Collection;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class ServiceUtils {

    public static User convertUserInputDtoToUser(int id, UserInputDto userInputDto) {
        var name = userInputDto.getName();
        return new User(id, name);
    }

    public static User convertUserInputDtoToUser(UserInputDto userInputDto) {
        int DEFAULT_USER_ID = 0;
        return convertUserInputDtoToUser(DEFAULT_USER_ID, userInputDto);
    }

    public static Cat convertCatInputDtoToCat(CatInputDto catInputDto) {
        var name = catInputDto.getName();
        var dateOfBirth = catInputDto.getDateOfBirth();
        return new Cat(name, dateOfBirth);
    }

    public static UserOutputDto convertUserToUserOutputDto(User user) {
        var id = user.getId();
        var name = user.getName();
        var cats = nonNull(user.getCats()) ? convertCatCollectionToCatOutputDtoCollection(user.getCats()) : null;
        return new UserOutputDto(id, name, cats);
    }

    public static Collection<UserOutputDto> convertUserCollectionToUserOutputDtoCollection(Collection<User> users) {
        return users.stream()
                .map(ServiceUtils::convertUserToUserOutputDto)
                .collect(toList());
    }

    public static CatOutputDto convertCatToCatOutputDto(Cat cat) {
        var id = cat.getId();
        var name = cat.getName();
        var dateOfBirth = cat.getDateOfBirth();
        var owner = nonNull(cat.getOwner()) ? convertUserToUserOutputDto(cat.getOwner()) : null;
        return new CatOutputDto(id, name, dateOfBirth, owner);
    }

    public static CatOutputDto convertCatToCatOutputDtoWithoutOwner(Cat cat) {
        var id = cat.getId();
        var name = cat.getName();
        var dateOfBirth = cat.getDateOfBirth();
        return new CatOutputDto(id, name, dateOfBirth, null);
    }

    public static Collection<CatOutputDto> convertCatCollectionToCatOutputDtoCollection(Collection<Cat> cats) {
        return cats.stream()
                .map(ServiceUtils::convertCatToCatOutputDtoWithoutOwner)
                .collect(toList());
    }
}