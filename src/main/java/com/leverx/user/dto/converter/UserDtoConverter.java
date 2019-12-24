package com.leverx.user.dto.converter;

import com.leverx.cat.repository.CatRepository;
import com.leverx.dog.repository.DogRepository;
import com.leverx.pet.dto.converter.PetDtoConverter;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.entity.User;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.*;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class UserDtoConverter {

    private final PetDtoConverter converter = new PetDtoConverter();
    private CatRepository catRepository;
    private DogRepository dogRepository;

    public User userInputDtoToUser(int id, UserInputDto userInputDto) {
        var name = userInputDto.getName();
        var email = userInputDto.getEmail();
        var animalPoints = userInputDto.getAnimalPoints();
        var cats = userInputDto.getCatsIds().stream()
                .map(catRepository::findById)
                .map(Optional::orElseThrow)
                .collect(toList());
        var dogs = userInputDto.getDogsIds().stream()
                .map(dogRepository::findById)
                .map(Optional::orElseThrow)
                .collect(toList());
        var pets = Stream.concat(cats.stream(), dogs.stream())
                .collect(toList());
        return new User(id, name, email, animalPoints, pets);
    }

    public User userInputDtoToUser(UserInputDto userInputDto) {
        var DEFAULT_USER_ID = 0;
        return userInputDtoToUser(DEFAULT_USER_ID, userInputDto);
    }

    public UserOutputDto userToUserOutputDto(User user) {
        var id = user.getId();
        var name = user.getName();
        var email = user.getEmail();
        var animalPoints = user.getAnimalPoints();
        var pets = nonNull(user.getPets()) ? converter.petCollectionToPetOutputDtoCollection(user.getPets()) : null;
        return new UserOutputDto(id, name, email, animalPoints, pets);
    }

    public Collection<UserOutputDto> userCollectionToUserOutputDtoCollection(Collection<User> users) {
        return users.stream()
                .map(this::userToUserOutputDto)
                .collect(toList());
    }
}