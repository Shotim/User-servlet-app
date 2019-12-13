package com.leverx.entity.pet.repository;

import com.leverx.entity.pet.entity.Pet;

import java.util.Collection;
import java.util.Optional;

public interface PetRepository {

    Collection<Pet> findAll();

    Optional<Pet> findById(int id);

    Collection<Pet> findByOwner(int ownerId);
}
