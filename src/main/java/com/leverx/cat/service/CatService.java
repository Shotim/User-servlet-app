package com.leverx.cat.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatInputDto;

import java.util.Collection;

public interface CatService {

    Collection<Cat> findAll();

    Cat findById(int id);

    Collection<Cat> findByOwner(int ownerId);

    Cat save(CatInputDto cat);
}
