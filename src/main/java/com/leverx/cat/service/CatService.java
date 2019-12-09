package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.validator.ValidationFailedException;

import java.util.Collection;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id) throws ElementNotFoundException;

    Collection<CatOutputDto> findByOwner(int ownerId);

    CatOutputDto save(CatInputDto cat) throws ValidationFailedException;

    void update(int catId, int ownerId);
}
