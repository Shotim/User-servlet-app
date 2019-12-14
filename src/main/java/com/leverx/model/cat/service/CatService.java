package com.leverx.model.cat.service;

import com.leverx.model.cat.dto.CatInputDto;
import com.leverx.model.cat.dto.CatOutputDto;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;

import java.util.Collection;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id) throws ElementNotFoundException;

    Collection<CatOutputDto> findByOwner(int id);

    CatOutputDto save(CatInputDto cat) throws ValidationFailedException;
}
