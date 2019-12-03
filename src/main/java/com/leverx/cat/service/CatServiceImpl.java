package com.leverx.cat.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static com.leverx.utils.ServiceUtils.convertCatCollectionToCatOutputDtoCollection;
import static com.leverx.utils.ServiceUtils.convertCatInputDtoToCat;
import static com.leverx.utils.ServiceUtils.convertCatToCatOutputDto;
import static com.leverx.validator.EntityValidator.isValid;

@Slf4j
public class CatServiceImpl implements CatService {

    private CatRepository catRepository = new CatRepositoryImpl();

    @Override
    public Collection<CatOutputDto> findAll() {
        var cats = catRepository.findAll();
        return convertCatCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto findById(int id) {
        var cat = catRepository.findById(id);
        return convertCatToCatOutputDto(cat);
    }

    @Override
    public Collection<CatOutputDto> findByOwner(int ownerId) {
        var cats = catRepository.findByOwner(ownerId);
        return convertCatCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto save(CatInputDto catInputDto) {
        if (isValid(catInputDto)) {
            Cat cat = convertCatInputDtoToCat(catInputDto);
            catRepository.save(cat);
            return convertCatToCatOutputDto(cat);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
