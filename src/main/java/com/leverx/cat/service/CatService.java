package com.leverx.cat.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatDto;

import java.util.Collection;

public interface CatService {

    Collection<Cat> findAll();

    Cat findById(int id);

    Collection<Cat> findByOwner(String ownerName);

    Cat save(CatDto cat);
}
