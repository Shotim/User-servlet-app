package com.leverx.model.pet.repository;

import com.leverx.model.pet.entity.Pet;

import java.util.Collection;
import java.util.Optional;

public interface PetRepository extends PetRepositoryI {

    default Collection<Pet> findAll() {
        return PetRepositoryI.super.findAll(Pet.class);
    }

    default Optional<Pet> findById(int id) {
        return PetRepositoryI.super.findById(id, Pet.class);
    }

    default Collection<Pet> findByOwner(int ownerId) {
        return PetRepositoryI.super.findByOwner(ownerId, Pet.class);
    }
}
