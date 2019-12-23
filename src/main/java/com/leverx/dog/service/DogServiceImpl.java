package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.dto.converter.DogDtoConverter;
import com.leverx.dog.repository.DogRepository;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.validator.EntityValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class DogServiceImpl implements DogService {

    private EntityValidator validator;
    private DogRepository dogRepository;
    private final DogDtoConverter converter = new DogDtoConverter();

    @Override
    public Collection<DogOutputDto> findAll() {
        var cats = dogRepository.findAll();
        return converter.dogCollectionToDogOutputDtoCollection(cats);
    }

    @Override
    public DogOutputDto findById(int id) throws ElementNotFoundException {
        var optionalCat = dogRepository.findById(id);
        var cat = optionalCat.orElseThrow(ElementNotFoundException::new);
        return converter.dogToDogOutputDto(cat);
    }

    @Override
    public Collection<DogOutputDto> findByOwner(int id) {
        var dogs = dogRepository.findByOwner(id);
        return converter.dogCollectionToDogOutputDtoCollection(dogs);
    }

    @Override
    public DogOutputDto save(DogInputDto dogInputDto) throws ValidationFailedException {
        validator.validateEntity(dogInputDto);
        var dog = converter.dogInputDtoToDog(dogInputDto);
        dogRepository.save(dog);
        return converter.dogToDogOutputDto(dog);
    }
}
