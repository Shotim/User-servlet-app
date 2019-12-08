package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.validator.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.NoSuchElementException;

import static com.leverx.cat.dto.converter.CatDtoConverter.catCollectionToCatOutputDtoCollection;
import static com.leverx.cat.dto.converter.CatDtoConverter.catInputDtoToCat;
import static com.leverx.cat.dto.converter.CatDtoConverter.catToCatOutputDto;
import static com.leverx.validator.EntityValidator.validateEntity;

@Slf4j
public class CatServiceImpl implements CatService {

    private CatRepository catRepository = new CatRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public Collection<CatOutputDto> findAll() {
        var cats = catRepository.findAll();
        return catCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto findById(int id) throws NoSuchElementException {
        var optionalCat = catRepository.findById(id);
        var cat = optionalCat.orElseThrow();
        return catToCatOutputDto(cat);
    }

    @Override
    public Collection<CatOutputDto> findByOwner(int ownerId) {
        var cats = catRepository.findByOwner(ownerId);
        return catCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto save(CatInputDto catInputDto) throws ValidationFailedException {
        validateEntity(catInputDto);
        var cat = catInputDtoToCat(catInputDto);
        catRepository.save(cat);
        return catToCatOutputDto(cat);
    }

    @Override
    public void update(int catId, int ownerId) {
        var cat = catRepository.findById(catId).orElseThrow();
        var user = userRepository.findById(ownerId).orElseThrow();
        cat.setOwner(user);
        catRepository.update(cat);
    }
}
