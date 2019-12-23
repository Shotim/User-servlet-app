package com.leverx.dog.dto.converter;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.entity.Dog;
import com.leverx.pet.dto.converter.PetDtoConverter;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class DogDtoConverter {

    private final PetDtoConverter converter = new PetDtoConverter();

    public Dog dogInputDtoToDog(DogInputDto dogInputDto) {
        var name = dogInputDto.getName();
        var dateOfBirth = dogInputDto.getDateOfBirth();
        var isCutEars = dogInputDto.isCutEars();
        return new Dog(name, dateOfBirth, isCutEars);
    }

    public DogOutputDto dogToDogOutputDto(Dog dog) {

        var dogOutputDto = converter.petToPetOutputDto(dog, DogOutputDto.class);
        var isCutEars = dog.isCutEars();
        dogOutputDto.setCutEars(isCutEars);
        return dogOutputDto;
    }

    public Collection<DogOutputDto> dogCollectionToDogOutputDtoCollection(Collection<Dog> dogs) {
        return dogs.stream()
                .map(this::dogToDogOutputDto)
                .collect(toList());
    }
}
