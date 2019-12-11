package com.leverx.entity.cat.service;

import com.leverx.entity.cat.dto.CatInputDto;
import com.leverx.entity.cat.dto.CatOutputDto;
import com.leverx.entity.cat.repository.CatRepository;
import com.leverx.entity.cat.repository.CatRepositoryImpl;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static com.leverx.entity.cat.dto.converter.CatDtoConverter.catCollectionToCatOutputDtoCollection;
import static com.leverx.entity.cat.dto.converter.CatDtoConverter.catInputDtoToCat;
import static com.leverx.entity.cat.dto.converter.CatDtoConverter.catToCatOutputDto;
import static com.leverx.validator.EntityValidator.validateEntity;

@Slf4j
public class CatServiceImpl implements CatService {

    private CatRepository catRepository = new CatRepositoryImpl();

    @Override
    public Collection<CatOutputDto> findAll() {
        var cats = catRepository.findAll();
        return catCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto findById(int id) throws ElementNotFoundException {
        var optionalCat = catRepository.findById(id);
        var cat = optionalCat.orElseThrow(ElementNotFoundException::new);
        return catToCatOutputDto(cat);
    }

    @Override
    public CatOutputDto save(CatInputDto catInputDto) throws ValidationFailedException {
        validateEntity(catInputDto);
        var cat = catInputDtoToCat(catInputDto);
        catRepository.save(cat);
        return catToCatOutputDto(cat);
    }
}
