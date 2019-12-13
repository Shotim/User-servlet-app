package com.leverx.entity.pet.service;

import com.leverx.entity.pet.dto.PetOutputDto;
import com.leverx.exception.ElementNotFoundException;

import java.util.Collection;

public interface PetService {

    Collection<PetOutputDto> findAll();

    PetOutputDto findById(int id) throws ElementNotFoundException;

    Collection<PetOutputDto> findByOwner(int id);
}
