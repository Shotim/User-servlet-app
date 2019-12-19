package com.leverx.model.dog.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.dog.dto.DogInputDto;
import com.leverx.model.dog.dto.DogOutputDto;

import java.util.Collection;

public interface DogService {

    Collection<DogOutputDto> findAll();

    DogOutputDto findById(int id) throws ElementNotFoundException;

    Collection<DogOutputDto> findByOwner(int id);

    DogOutputDto save(DogInputDto cat) throws ValidationFailedException;
}
