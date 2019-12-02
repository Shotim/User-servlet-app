package com.leverx.cat.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatInputDto;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static com.leverx.utils.ServiceUtils.convertCatDtoToCat;
import static com.leverx.validator.EntityValidator.isValid;

@Slf4j
public class CatServiceImpl implements CatService {

    private CatRepository catRepository = new CatRepositoryImpl();

    @Override
    public Collection<Cat> findAll() {
        Collection<Cat> cats = catRepository.findAll();
        return cats;
    }

    @Override
    public Cat findById(int id) {
        Cat cat = catRepository.findById(id);
        return cat;
    }

    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        Collection<Cat> cats = catRepository.findByOwner(ownerId);
        return cats;
    }

    @Override
    public Cat save(CatInputDto catInputDto) {
        if (isValid(catInputDto)) {
            Cat cat = convertCatDtoToCat(catInputDto);
            catRepository.save(cat);
            return cat;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
