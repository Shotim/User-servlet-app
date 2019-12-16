package com.leverx.model.dog.service;

import com.leverx.difactory.Injectable;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.dog.dto.DogInputDto;
import com.leverx.model.dog.dto.DogOutputDto;
import com.leverx.model.dog.repository.DogRepository;

import java.util.Collection;

import static com.leverx.difactory.DIFactory.getInstance;
import static com.leverx.model.dog.dto.converter.DogDtoConverter.dogCollectionToDogOutputDtoCollection;
import static com.leverx.model.dog.dto.converter.DogDtoConverter.dogInputDtoToDog;
import static com.leverx.model.dog.dto.converter.DogDtoConverter.dogToDogOutputDto;
import static com.leverx.validator.EntityValidator.validateEntity;

@Injectable
public class DogServiceImpl implements DogService {

    private DogRepository dogRepository;

    public DogServiceImpl() {
        var diFactory = getInstance();
        dogRepository = (DogRepository) diFactory.getBean(DogRepository.class);
    }

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
