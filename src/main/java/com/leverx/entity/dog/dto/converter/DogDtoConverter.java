package com.leverx.entity.dog.dto.converter;

import com.leverx.entity.dog.dto.DogInputDto;
import com.leverx.entity.dog.dto.DogOutputDto;
import com.leverx.entity.dog.entity.Dog;
import com.leverx.entity.user.entity.User;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class DogDtoConverter {

    public static Dog dogInputDtoToDog(DogInputDto dogInputDto) {
        var name = dogInputDto.getName();
        var dateOfBirth = dogInputDto.getDateOfBirth();
        var isCutEars = dogInputDto.isCutEars();
        return new Dog(name, dateOfBirth, isCutEars);
    }

    public static DogOutputDto dogToDogOutputDto(Dog dog) {
        var id = dog.getId();
        var name = dog.getName();
        var dateOfBirth = dog.getDateOfBirth();
        var isCutEars = dog.isCutEars();
        var owners = dog.getOwners();
        var ownerIds = owners.stream()
                .map(User::getId)
                .collect(toList());
        return new DogOutputDto(id, name, dateOfBirth, isCutEars, ownerIds);
    }

    public static Collection<DogOutputDto> dogCollectionToDogOutputDtoCollection(Collection<Dog> dogs) {
        return dogs.stream()
                .map(DogDtoConverter::dogToDogOutputDto)
                .collect(toList());
    }
}
