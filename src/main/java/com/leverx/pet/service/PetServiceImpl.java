package com.leverx.pet.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.repository.PetRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;

import static com.leverx.pet.dto.converter.PetDtoConverter.petCollectionToPetOutputDtoCollection;
import static com.leverx.pet.dto.converter.PetDtoConverter.petToPetOutputDto;


@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private PetRepository petRepository;

    @Override
    public Collection<PetOutputDto> findAll() {
        var cats = petRepository.findAll();
        return petCollectionToPetOutputDtoCollection(cats);
    }

    @Override
    public PetOutputDto findById(int id) throws ElementNotFoundException {
        var optionalPet = petRepository.findById(id);
        var pet = optionalPet.orElseThrow(ElementNotFoundException::new);
        return petToPetOutputDto(pet);
    }

    @Override
    public Collection<PetOutputDto> findByOwner(int id) {
        var pets = petRepository.findByOwner(id);
        return petCollectionToPetOutputDtoCollection(pets);
    }
}
