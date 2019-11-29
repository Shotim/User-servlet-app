package com.leverx.cat.repository;

import com.leverx.cat.entity.Cat;

import java.util.Collection;

public interface CatRepository {

    Collection<Cat> findAll();

    Collection<Cat> findByOwner(int ownerId);

    Cat findById(int id);

    Cat save(Cat cat);

    Cat update(Cat cat);
}
