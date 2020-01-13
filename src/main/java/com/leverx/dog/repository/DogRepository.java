package com.leverx.dog.repository;

import com.leverx.dog.entity.Dog;

import java.util.Collection;
import java.util.Optional;

public interface DogRepository {

    Collection<Dog> findAll();

    Optional<Dog> findById(int id);

    Collection<Dog> findByOwner(int ownerId);

    Optional<Dog> save(Dog dog);
}
