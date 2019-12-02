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
        return catRepository.findAll();
    }

    @Override
    public Cat findById(int id) {
        return catRepository.findById(id);
    }

    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        return catRepository.findByOwner(ownerId);
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
