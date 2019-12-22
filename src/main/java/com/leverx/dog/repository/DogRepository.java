package com.leverx.dog.repository;

import com.leverx.dog.entity.Dog;
import com.leverx.pet.repository.PetGenericRepository;

import java.util.Collection;
import java.util.Optional;

public interface DogRepository extends PetGenericRepository {

    default Collection<Dog> findAll() {
        return PetGenericRepository.super.findAll(Dog.class);
    }

    default Optional<Dog> findById(int id) {
        return PetGenericRepository.super.findById(id, Dog.class);
    }

    default Collection<Dog> findByOwner(int ownerId) {
        return PetGenericRepository.super.findByOwner(ownerId, Dog.class);
    }

    Optional<Dog> save(Dog cat);
}
