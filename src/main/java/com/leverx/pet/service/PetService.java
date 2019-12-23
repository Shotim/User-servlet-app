package com.leverx.pet.service;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.pet.dto.PetOutputDto;

import java.util.Collection;

public interface PetService {

    Collection<PetOutputDto> findAll();

    PetOutputDto findById(int id) throws ElementNotFoundException;

    Collection<PetOutputDto> findByOwner(int id);
}
