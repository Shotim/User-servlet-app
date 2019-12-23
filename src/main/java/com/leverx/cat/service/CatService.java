package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;

import java.util.Collection;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id) throws ElementNotFoundException;

    Collection<CatOutputDto> findByOwner(int id);

    CatOutputDto save(CatInputDto cat) throws ValidationFailedException;
}
