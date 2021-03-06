package com.leverx.pet.service;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.dto.converter.PetDtoConverter;
import com.leverx.pet.repository.PetRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetDtoConverter converter = new PetDtoConverter();
    private PetRepository petRepository;

    @Override
    public Collection<PetOutputDto> findAll() {
        var pets = petRepository.findAll();
        return converter.petCollectionToPetOutputDtoCollection(pets);
    }

    @Override
    public PetOutputDto findById(int id) {
        var optionalPet = petRepository.findById(id);
        var pet = optionalPet.orElseThrow(ElementNotFoundException::new);
        return converter.petToPetOutputDto(pet);
    }

    @Override
    public Collection<PetOutputDto> findByOwner(int id) {
        var pets = petRepository.findByOwner(id);
        return converter.petCollectionToPetOutputDtoCollection(pets);
    }
}
