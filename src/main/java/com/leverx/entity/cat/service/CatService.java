package com.leverx.entity.cat.service;

import com.leverx.entity.cat.dto.CatInputDto;
import com.leverx.entity.cat.dto.CatOutputDto;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;

import java.util.Collection;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id) throws ElementNotFoundException;

    Collection<CatOutputDto> findByOwner(int ownerId);

    CatOutputDto save(CatInputDto cat) throws ValidationFailedException;
}
