package com.leverx.entity.dog.dto.converter;

import com.leverx.entity.dog.dto.DogInputDto;
import com.leverx.entity.dog.dto.DogOutputDto;
import com.leverx.entity.dog.entity.Dog;

import java.util.Collection;

import static com.leverx.entity.pet.dto.converter.PetDtoConverter.petToPetOutputDto;
import static java.util.stream.Collectors.toList;

public class DogDtoConverter {

    public static Dog dogInputDtoToDog(DogInputDto dogInputDto) {
        var name = dogInputDto.getName();
        var dateOfBirth = dogInputDto.getDateOfBirth();
        var isCutEars = dogInputDto.isCutEars();
        return new Dog(name, dateOfBirth, isCutEars);
    }

    public static DogOutputDto dogToDogOutputDto(Dog dog) {

        var dogOutputDto = petToPetOutputDto(dog, DogOutputDto.class);
        var isCutEars = dog.isCutEars();
        dogOutputDto.setCutEars(isCutEars);
        return dogOutputDto;
    }

    public static Collection<DogOutputDto> dogCollectionToDogOutputDtoCollection(Collection<Dog> dogs) {
        return dogs.stream()
                .map(DogDtoConverter::dogToDogOutputDto)
                .collect(toList());
    }
}
