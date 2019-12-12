package com.leverx.entity.cat.dto.converter;

import com.leverx.entity.cat.dto.CatInputDto;
import com.leverx.entity.cat.dto.CatOutputDto;
import com.leverx.entity.cat.entity.Cat;
import com.leverx.entity.user.entity.User;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class CatDtoConverter {

    public static Cat catInputDtoToCat(CatInputDto catInputDto) {
        var name = catInputDto.getName();
        var dateOfBirth = catInputDto.getDateOfBirth();
        int miceCachedNumber = catInputDto.getMiceCachedNumber();
        return new Cat(name, dateOfBirth, miceCachedNumber);
    }

    public static CatOutputDto catToCatOutputDto(Cat cat) {
        //TODO Refactoring
        var id = cat.getId();
        var name = cat.getName();
        var dateOfBirth = cat.getDateOfBirth();
        var miceCachedNumber = cat.getMiceCachedNumber();
        var owners = cat.getOwners();
        var ownerIds = owners.stream()
                .map(User::getId)
                .collect(toList());
        return new CatOutputDto(id, name, dateOfBirth, miceCachedNumber, ownerIds);

    }

    public static Collection<CatOutputDto> catCollectionToCatOutputDtoCollection(Collection<Cat> cats) {
        return cats.stream()
                .map(CatDtoConverter::catToCatOutputDto)
                .collect(toList());
    }
}