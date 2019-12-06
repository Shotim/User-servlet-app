package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;

import java.util.Collection;
import java.util.List;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id);

    Collection<CatOutputDto> findByOwner(int ownerId);

    CatOutputDto save(CatInputDto cat);

    void assignCatsToExistingUser(int ownerId, List<Integer> catsIds);

    void update(int catId, int ownerId);
}
