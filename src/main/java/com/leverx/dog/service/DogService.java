package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;

import java.util.Collection;

public interface DogService {

    Collection<DogOutputDto> findAll();

    DogOutputDto findById(int id);

    Collection<DogOutputDto> findByOwner(int id);

    DogOutputDto save(DogInputDto cat);
}
