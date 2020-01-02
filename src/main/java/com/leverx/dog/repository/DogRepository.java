package com.leverx.dog.repository;

import com.leverx.dog.entity.Dog;

import java.util.Collection;
import java.util.Optional;

public interface DogRepository {

    Collection<Dog> findAll();

    Optional<Dog> findById(int id);

    Optional<Dog> save(Dog cat);
}
