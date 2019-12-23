package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;

import java.util.Collection;

public interface DogService {

    Collection<DogOutputDto> findAll();

    DogOutputDto findById(int id) throws ElementNotFoundException;

    Collection<DogOutputDto> findByOwner(int id);

    DogOutputDto save(DogInputDto cat) throws ValidationFailedException;
}
