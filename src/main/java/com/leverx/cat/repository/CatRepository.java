package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.pet.repository.PetGenericRepository;

import java.util.Collection;
import java.util.Optional;

public interface CatRepository extends PetGenericRepository {

    default Collection<Cat> findAll() {
        return PetGenericRepository.super.findAll(Cat.class);
    }

    default Optional<Cat> findById(int id) {
        return PetGenericRepository.super.findById(id, Cat.class);
    }

    default Collection<Cat> findByOwner(int ownerId) {
        return PetGenericRepository.super.findByOwner(ownerId, Cat.class);
    }

    Optional<Cat> save(Cat cat);
}
