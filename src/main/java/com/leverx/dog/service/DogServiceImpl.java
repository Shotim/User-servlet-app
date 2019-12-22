package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.repository.DogRepository;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.validator.EntityValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;

import static com.leverx.dog.dto.converter.DogDtoConverter.dogCollectionToDogOutputDtoCollection;
import static com.leverx.dog.dto.converter.DogDtoConverter.dogInputDtoToDog;
import static com.leverx.dog.dto.converter.DogDtoConverter.dogToDogOutputDto;


@AllArgsConstructor
public class DogServiceImpl implements DogService {

    private EntityValidator validator;
    private DogRepository dogRepository;

    @Override
    public Collection<DogOutputDto> findAll() {
        var cats = dogRepository.findAll();
        return dogCollectionToDogOutputDtoCollection(cats);
    }

    @Override
    public DogOutputDto findById(int id) throws ElementNotFoundException {
        var optionalCat = dogRepository.findById(id);
        var cat = optionalCat.orElseThrow(ElementNotFoundException::new);
        return dogToDogOutputDto(cat);
    }

    @Override
    public Collection<DogOutputDto> findByOwner(int id) {
        var dogs = dogRepository.findByOwner(id);
        return dogCollectionToDogOutputDtoCollection(dogs);
    }

    @Override
    public DogOutputDto save(DogInputDto dogInputDto) throws ValidationFailedException {
        validator.validateEntity(dogInputDto);
        var dog = dogInputDtoToDog(dogInputDto);
        dogRepository.save(dog);
        return dogToDogOutputDto(dog);
    }
}
