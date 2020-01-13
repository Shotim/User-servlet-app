package com.leverx.dog.service;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.validator.EntityValidator;
import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.dto.converter.DogDtoConverter;
import com.leverx.dog.repository.DogRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogDtoConverter converter = new DogDtoConverter();
    private EntityValidator validator;
    private DogRepository dogRepository;

    @Override
    public Collection<DogOutputDto> findAll() {
        var cats = dogRepository.findAll();
        return converter.dogCollectionToDogOutputDtoCollection(cats);
    }

    @Override
    public DogOutputDto findById(int id) {
        var optionalDog = dogRepository.findById(id);
        var dog = optionalDog.orElseThrow(ElementNotFoundException::new);
        return converter.dogToDogOutputDto(dog);
    }

    @Override
    public Collection<DogOutputDto> findByOwner(int id) {
        var dogs = dogRepository.findByOwner(id);
        return converter.dogCollectionToDogOutputDtoCollection(dogs);
    }

    @Override
    public DogOutputDto save(DogInputDto dogInputDto) {
        validator.validateEntity(dogInputDto);
        var dog = converter.dogInputDtoToDog(dogInputDto);
        dogRepository.save(dog);
        return converter.dogToDogOutputDto(dog);
    }
}
