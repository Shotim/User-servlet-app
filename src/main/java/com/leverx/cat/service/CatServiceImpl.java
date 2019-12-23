package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.dto.converter.CatDtoConverter;
import com.leverx.cat.repository.CatRepository;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.validator.EntityValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class CatServiceImpl implements CatService {

    private EntityValidator validator;
    private CatRepository catRepository;
    private final CatDtoConverter converter = new CatDtoConverter();

    @Override
    public Collection<CatOutputDto> findAll() {
        var cats = catRepository.findAll();
        return converter.catCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto findById(int id) throws ElementNotFoundException {
        var optionalCat = catRepository.findById(id);
        var cat = optionalCat.orElseThrow(ElementNotFoundException::new);
        return converter.catToCatOutputDto(cat);
    }

    @Override
    public Collection<CatOutputDto> findByOwner(int id) {
        var cats = catRepository.findByOwner(id);
        return converter.catCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto save(CatInputDto catInputDto) throws ValidationFailedException {
        validator.validateEntity(catInputDto);
        var cat = converter.catInputDtoToCat(catInputDto);
        catRepository.save(cat);
        return converter.catToCatOutputDto(cat);
    }
}
