package com.leverx.pet.service;

import com.leverx.pet.dto.PetOutputDto;

import java.util.Collection;

public interface PetService {

    Collection<PetOutputDto> findAll();

    PetOutputDto findById(int id);

    Collection<PetOutputDto> findByOwner(int id);
}
