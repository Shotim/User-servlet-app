package com.leverx.entity.dog.service;

import com.leverx.entity.dog.dto.DogInputDto;
import com.leverx.entity.dog.dto.DogOutputDto;
import com.leverx.entity.dog.repository.DogRepository;
import com.leverx.entity.dog.repository.DogRepositoryImpl;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;

import java.util.Collection;

import static com.leverx.entity.dog.dto.converter.DogDtoConverter.dogCollectionToDogOutputDtoCollection;
import static com.leverx.entity.dog.dto.converter.DogDtoConverter.dogInputDtoToDog;
import static com.leverx.entity.dog.dto.converter.DogDtoConverter.dogToDogOutputDto;
import static com.leverx.validator.EntityValidator.validateEntity;

public class DogServiceImpl implements DogService {

    private DogRepository dogRepository = new DogRepositoryImpl();

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
        validateEntity(dogInputDto);
        var dog = dogInputDtoToDog(dogInputDto);
        dogRepository.save(dog);
        return dogToDogOutputDto(dog);
    }
}
