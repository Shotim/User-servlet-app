package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.repository.CatRepository;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.validator.EntityValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;

import static com.leverx.cat.dto.converter.CatDtoConverter.catCollectionToCatOutputDtoCollection;
import static com.leverx.cat.dto.converter.CatDtoConverter.catInputDtoToCat;
import static com.leverx.cat.dto.converter.CatDtoConverter.catToCatOutputDto;


@AllArgsConstructor
public class CatServiceImpl implements CatService {

    private CatRepository catRepository;
    private final EntityValidator validator = new EntityValidator();

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
    public Collection<CatOutputDto> findByOwner(int id) {
        var cats = catRepository.findByOwner(id);
        return catCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto save(CatInputDto catInputDto) throws ValidationFailedException {
        validator.validateEntity(catInputDto);
        var cat = catInputDtoToCat(catInputDto);
        catRepository.save(cat);
        return catToCatOutputDto(cat);
    }
}
