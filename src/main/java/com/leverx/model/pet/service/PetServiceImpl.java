package com.leverx.model.pet.service;

import com.leverx.model.pet.dto.PetOutputDto;
import com.leverx.model.pet.repository.PetRepository;
import com.leverx.model.pet.repository.PetRepositoryImpl;
import com.leverx.exception.ElementNotFoundException;

import java.util.Collection;

import static com.leverx.model.pet.dto.converter.PetDtoConverter.petCollectionToPetOutputDtoCollection;
import static com.leverx.model.pet.dto.converter.PetDtoConverter.petToPetOutputDto;

public class PetServiceImpl implements PetService {

    private PetRepository petRepository = new PetRepositoryImpl();

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
