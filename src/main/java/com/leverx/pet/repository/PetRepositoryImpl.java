package com.leverx.pet.repository;

import com.leverx.pet.entity.Pet;

import java.util.Collection;
import java.util.Optional;


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
