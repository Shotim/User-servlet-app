package com.leverx.entity.dog.service;

import com.leverx.entity.dog.dto.DogInputDto;
import com.leverx.entity.dog.dto.DogOutputDto;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;

import java.util.Collection;

public interface DogService {

    Collection<DogOutputDto> findAll();

    DogOutputDto findById(int id) throws ElementNotFoundException;

    Collection<DogOutputDto> findByOwner(int ownerId);

    DogOutputDto save(DogInputDto cat) throws ValidationFailedException;
}
