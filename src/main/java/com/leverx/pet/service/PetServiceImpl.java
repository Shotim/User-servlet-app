package com.leverx.pet.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.dto.converter.PetDtoConverter;
import com.leverx.pet.repository.PetRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private PetRepository petRepository;
    private final PetDtoConverter converter = new PetDtoConverter();

    @Override
    public Collection<PetOutputDto> findAll() {
        var pets = petRepository.findAll();
        return converter.petCollectionToPetOutputDtoCollection(pets);
    }

    @Override
    public PetOutputDto findById(int id) throws ElementNotFoundException {
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
