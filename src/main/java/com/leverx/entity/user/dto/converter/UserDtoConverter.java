package com.leverx.entity.user.dto.converter;

import com.leverx.entity.cat.repository.CatRepository;
import com.leverx.entity.cat.repository.CatRepositoryImpl;
import com.leverx.entity.user.dto.UserInputDto;
import com.leverx.entity.user.dto.UserOutputDto;
import com.leverx.entity.user.entity.User;

import java.util.Collection;
import java.util.Optional;

import static com.leverx.entity.cat.dto.converter.CatDtoConverter.catCollectionToCatOutputDtoCollection;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserDtoConverter {

    private static final CatRepository catRepository = new CatRepositoryImpl();

    public static User userInputDtoToUser(int id, UserInputDto userInputDto) {
        var name = userInputDto.getName();
        var cats = userInputDto.getCatsIds().stream()
                .map(catRepository::findById)
                .map(Optional::orElseThrow)
                .collect(toList());
        return new User(id, name, cats);
    }

    public static User userInputDtoToUser(UserInputDto userInputDto) {
        var DEFAULT_USER_ID = 0;
        return userInputDtoToUser(DEFAULT_USER_ID, userInputDto);
    }

    public static UserOutputDto userToUserOutputDto(User user) {
        var id = user.getId();
        var name = user.getName();
        var cats = nonNull(user.getCats()) ? catCollectionToCatOutputDtoCollection(user.getCats()) : null;
        return new UserOutputDto(id, name, cats);
    }

    public static Collection<UserOutputDto> userCollectionToUserOutputDtoCollection(Collection<User> users) {
        return users.stream()
                .map(UserDtoConverter::userToUserOutputDto)
                .collect(toList());
    }
}