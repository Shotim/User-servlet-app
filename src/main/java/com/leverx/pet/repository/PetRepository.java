package com.leverx.pet.repository;

import com.leverx.pet.entity.Pet;

import java.util.Collection;
import java.util.Optional;

public interface PetRepository extends PetGenericRepository {

    default Collection<Pet> findAll() {
        return PetGenericRepository.super.findAll(Pet.class);
    }

    default Optional<Pet> findById(int id) {
        return PetGenericRepository.super.findById(id, Pet.class);
    }

    default Collection<Pet> findByOwner(int ownerId) {
        return PetGenericRepository.super.findByOwner(ownerId, Pet.class);
    }
}
