package com.leverx.model.cat.repository;

import com.leverx.model.cat.entity.Cat;
import com.leverx.model.pet.repository.PetRepositoryI;

import java.util.Collection;
import java.util.Optional;

public interface CatRepository extends PetRepositoryI {

    default Collection<Cat> findAll() {
        return PetRepositoryI.super.findAll(Cat.class);
    }

    default Optional<Cat> findById(int id) {
        return PetRepositoryI.super.findById(id, Cat.class);
    }

    default Collection<Cat> findByOwner(int ownerId) {
        return PetRepositoryI.super.findByOwner(ownerId, Cat.class);
    }

    Optional<Cat> save(Cat cat);
}
