package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static com.leverx.cat.dto.CatDtoConverter.convertCatCollectionToCatOutputDtoCollection;
import static com.leverx.cat.dto.CatDtoConverter.convertCatInputDtoToCat;
import static com.leverx.cat.dto.CatDtoConverter.convertCatToCatOutputDto;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class CatServiceImpl implements CatService {

    private CatRepository catRepository = new CatRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public Collection<CatOutputDto> findAll() {
        var cats = catRepository.findAll();
        return convertCatCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto findById(int id) throws NoSuchElementException {
        var optionalCat = catRepository.findById(id);
        var cat = optionalCat.orElseThrow();
        return convertCatToCatOutputDto(cat);
    }

    @Override
    public Collection<CatOutputDto> findByOwner(int ownerId) {
        var cats = catRepository.findByOwner(ownerId);
        return convertCatCollectionToCatOutputDtoCollection(cats);
    }

    @Override
    public CatOutputDto save(CatInputDto catInputDto) {
        Cat cat = convertCatInputDtoToCat(catInputDto);
        catRepository.save(cat);
        return convertCatToCatOutputDto(cat);
    }

    @Override
    public void assignCatsToUser(int ownerId, List<Integer> catsIds) throws NoSuchElementException {
        var optionalUser = userRepository.findById(ownerId);
        var user = optionalUser.orElseThrow();
        var userCats = user.getCats();

        var cats = catsIds.stream()
                .map(this::findCatIfExist)
                .filter(cat -> isNull(cat.getOwner()))
                .collect(toSet());

        userCats.addAll(cats);
        userCats.forEach(cat -> cat.setOwner(user));
        user.setCats(userCats);
        userRepository.update(user);
    }

    private Cat findCatIfExist(Integer catId) {
        return catRepository.findById(catId).orElseThrow();
    }
}
