package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;

import java.util.Collection;
import java.util.Optional;

public interface CatRepository {

    Collection<Cat> findAll();

    Collection<Cat> findByOwner(int ownerId);

    Optional<Cat> findById(int id);

    Optional<Cat> save(Cat cat);

    void update(Cat cat);
}
