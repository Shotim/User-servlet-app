package com.leverx.cat.dto.converter;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;

import java.util.Collection;

import static com.leverx.user.dto.converter.UserDtoConverter.convertUserToUserOutputDto;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class CatDtoConverter {

    public static Cat convertCatInputDtoToCat(CatInputDto catInputDto) {
        var name = catInputDto.getName();
        var dateOfBirth = catInputDto.getDateOfBirth();
        return new Cat(name, dateOfBirth);
    }

    public static CatOutputDto convertCatToCatOutputDto(Cat cat) {
        var id = cat.getId();
        var name = cat.getName();
        var dateOfBirth = cat.getDateOfBirth();
        var owner = nonNull(cat.getOwner()) ? convertUserToUserOutputDto(cat.getOwner()) : null;
        return new CatOutputDto(id, name, dateOfBirth, owner);
    }

    public static CatOutputDto convertCatToCatOutputDtoWithoutOwner(Cat cat) {
        var id = cat.getId();
        var name = cat.getName();
        var dateOfBirth = cat.getDateOfBirth();
        return new CatOutputDto(id, name, dateOfBirth, null);
    }

    public static Collection<CatOutputDto> convertCatCollectionToCatOutputDtoCollection(Collection<Cat> cats) {
        return cats.stream()
                .map(CatDtoConverter::convertCatToCatOutputDtoWithoutOwner)
                .collect(toList());
    }
}
