package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;

import java.util.Collection;
import java.util.Optional;

public interface CatRepository {

    Collection<Cat> findAll();

    Optional<Cat> findById(int id);

    Optional<Cat> save(Cat cat);
}
