package com.leverx.entity.dog.repository;

import com.leverx.entity.dog.entity.Dog;

import java.util.Collection;
import java.util.Optional;

public interface DogRepository {

    Collection<Dog> findAll();

    Collection<Dog> findByOwner(int ownerId);

    Optional<Dog> findById(int id);

    Optional<Dog> save(Dog cat);
}
