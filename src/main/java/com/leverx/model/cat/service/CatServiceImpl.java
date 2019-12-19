package com.leverx.model.cat.service;

import com.leverx.difactory.Injectable;
import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.cat.dto.CatInputDto;
import com.leverx.model.cat.dto.CatOutputDto;
import com.leverx.model.cat.repository.CatRepository;
import com.leverx.validator.EntityValidator;

import java.util.Collection;

import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.model.cat.dto.converter.CatDtoConverter.catCollectionToCatOutputDtoCollection;
import static com.leverx.model.cat.dto.converter.CatDtoConverter.catInputDtoToCat;
import static com.leverx.model.cat.dto.converter.CatDtoConverter.catToCatOutputDto;

@Injectable
public class CatServiceImpl implements CatService {

    private CatRepository catRepository = getBean(CatRepository.class);
    private EntityValidator validator = new EntityValidator();

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
