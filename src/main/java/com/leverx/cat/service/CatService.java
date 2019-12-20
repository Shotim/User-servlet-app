package com.leverx.cat.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;

import java.util.Collection;

public interface CatService {

    Collection<CatOutputDto> findAll();

    CatOutputDto findById(int id) throws ElementNotFoundException;

    Collection<CatOutputDto> findByOwner(int id);

    CatOutputDto save(CatInputDto cat) throws ValidationFailedException;
}
