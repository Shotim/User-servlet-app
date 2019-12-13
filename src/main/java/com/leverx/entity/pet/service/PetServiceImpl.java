package com.leverx.entity.pet.service;

import com.leverx.entity.pet.dto.PetOutputDto;
import com.leverx.entity.pet.repository.PetRepository;
import com.leverx.entity.pet.repository.PetRepositoryImpl;
import com.leverx.exception.ElementNotFoundException;

import java.util.Collection;

import static com.leverx.entity.pet.dto.converter.PetDtoConverter.petCollectionToPetOutputDtoCollection;
import static com.leverx.entity.pet.dto.converter.PetDtoConverter.petToPetOutputDto;

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
