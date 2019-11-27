package com.leverx.cat.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatDto;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import org.slf4j.Logger;

import java.util.Collection;

import static com.leverx.utils.ServiceUtils.convertCatDtoToCat;
import static com.leverx.validator.EntityValidator.isValid;
import static org.slf4j.LoggerFactory.getLogger;

public class CatServiceImpl implements CatService {

    private static final Logger LOGGER = getLogger(CatServiceImpl.class);
    private CatRepository catRepository = new CatRepositoryImpl();

    @Override
    public Collection<Cat> findAll() {
        Collection<Cat> cats = catRepository.findAll();
        LOGGER.debug("Were received {} cats", cats.size());
        return cats;
    }

    @Override
    public Cat findById(int id) {
        Cat cat = catRepository.findById(id);
        LOGGER.debug("Was received cat with id = {}", id);
        return cat;
    }

    @Override
    public Collection<Cat> findByOwner(int ownerId) {
        Collection<Cat> cats = catRepository.findByOwner(ownerId);
        LOGGER.debug("Were received {} cats", cats.size());
        return cats;
    }

    @Override
    public Cat save(CatDto catDto) {
        if (isValid(catDto)) {
            Cat cat = convertCatDtoToCat(catDto);
            catRepository.save(cat);
            LOGGER.debug("Cat with name = {} was saved", catDto.getName());
            return cat;
        } else {
            LOGGER.error("Cat with name = {} was not saved", catDto.getName());
            throw new IllegalArgumentException();
        }
    }
}
