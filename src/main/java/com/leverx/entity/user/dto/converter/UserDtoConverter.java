package com.leverx.entity.user.dto.converter;

import com.leverx.entity.cat.repository.CatRepository;
import com.leverx.entity.cat.repository.CatRepositoryImpl;
import com.leverx.entity.dog.repository.DogRepository;
import com.leverx.entity.dog.repository.DogRepositoryImpl;
import com.leverx.entity.user.dto.UserInputDto;
import com.leverx.entity.user.dto.UserOutputDto;
import com.leverx.entity.user.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static com.leverx.entity.pet.dto.converter.PetDtoConverter.petCollectionToPetOutputDtoCollection;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserDtoConverter {

    private static final CatRepository catRepository = new CatRepositoryImpl();
    private static final DogRepository dogRepository = new DogRepositoryImpl();

    public static User userInputDtoToUser(int id, UserInputDto userInputDto) {
        var name = userInputDto.getName();
        var cats = userInputDto.getCatsIds().stream()
                .map(catRepository::findById)
                .map(Optional::orElseThrow)
                .collect(toList());
        var dogs = userInputDto.getCatsIds().stream()
                .map(dogRepository::findById)
                .map(Optional::orElseThrow)
                .collect(toList());
        var pets = Stream.concat(cats.stream(), dogs.stream())
                .collect(toList());
        return new User(id, name, pets);
    }

    public static User userInputDtoToUser(UserInputDto userInputDto) {
        var DEFAULT_USER_ID = 0;
        return userInputDtoToUser(DEFAULT_USER_ID, userInputDto);
    }

    public static UserOutputDto userToUserOutputDto(User user) {
        var id = user.getId();
        var name = user.getName();
        var pets = nonNull(user.getPets()) ? petCollectionToPetOutputDtoCollection(user.getPets()) : null;
        return new UserOutputDto(id, name, pets);
    }

    public static Collection<UserOutputDto> userCollectionToUserOutputDtoCollection(Collection<User> users) {
        return users.stream()
                .map(UserDtoConverter::userToUserOutputDto)
                .collect(toList());
    }
}