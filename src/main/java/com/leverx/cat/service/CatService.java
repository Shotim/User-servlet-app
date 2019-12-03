package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;

import java.util.Collection;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id);

    Collection<CatOutputDto> findByOwner(int ownerId);

    CatOutputDto save(CatInputDto cat);
}
