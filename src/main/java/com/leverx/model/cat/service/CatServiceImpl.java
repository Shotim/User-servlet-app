package com.leverx.model.cat.service;

import com.leverx.difactory.Injectable;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.cat.dto.CatInputDto;
import com.leverx.model.cat.dto.CatOutputDto;
import com.leverx.model.cat.repository.CatRepository;

import java.util.Collection;

import static com.leverx.difactory.DIFactory.getInstance;
import static com.leverx.model.cat.dto.converter.CatDtoConverter.catCollectionToCatOutputDtoCollection;
import static com.leverx.model.cat.dto.converter.CatDtoConverter.catInputDtoToCat;
import static com.leverx.model.cat.dto.converter.CatDtoConverter.catToCatOutputDto;
import static com.leverx.validator.EntityValidator.validateEntity;

@Injectable
public class CatServiceImpl implements CatService {

    private CatRepository catRepository;

    public CatServiceImpl() {
        var diFactory = getInstance();
        catRepository = (CatRepository) diFactory.getBean(CatRepository.class);
    }

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
        validateEntity(catInputDto);
        var cat = catInputDtoToCat(catInputDto);
        catRepository.save(cat);
        return catToCatOutputDto(cat);
    }
}
