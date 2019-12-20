package com.leverx.dog.repository;

import com.leverx.dog.entity.Dog;
import com.leverx.pet.repository.PetRepositoryI;

import java.util.Collection;
import java.util.Optional;

public interface DogRepository extends PetRepositoryI {

    default Collection<Dog> findAll() {
        return PetRepositoryI.super.findAll(Dog.class);
    }

    default Optional<Dog> findById(int id) {
        return PetRepositoryI.super.findById(id, Dog.class);
    }

    default Collection<Dog> findByOwner(int ownerId) {
        return PetRepositoryI.super.findByOwner(ownerId, Dog.class);
    }

    Optional<Dog> save(Dog cat);
}
