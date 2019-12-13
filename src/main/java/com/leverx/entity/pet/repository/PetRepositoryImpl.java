package com.leverx.entity.pet.repository;

import com.leverx.entity.pet.entity.Pet;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public class PetRepositoryImpl implements PetRepository {

    @Override
    public Collection<Pet> findAll() {
        return PetRepository.super.findAll();
    }

    @Override
    public Optional<Pet> findById(int id) {
        return PetRepository.super.findById(id);
    }

    @Override
    public Collection<Pet> findByOwner(int ownerId) {
        return PetRepository.super.findByOwner(ownerId);
    }
}
