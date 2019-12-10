package com.leverx.dog.dto.converter;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.entity.Dog;

import java.util.Collection;

import static java.util.Objects.nonNull;
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
        var owner = dog.getOwner();
        if (nonNull(owner)) {
            var ownerId = owner.getId();
            return new DogOutputDto(id, name, dateOfBirth, isCutEars, ownerId);
        }
        return new DogOutputDto(id, name, dateOfBirth, isCutEars, null);
    }

    public static Collection<DogOutputDto> dogCollectionToDogOutputDtoCollection(Collection<Dog> dogs) {
        return dogs.stream()
                .map(DogDtoConverter::dogToDogOutputDto)
                .collect(toList());
    }
}
