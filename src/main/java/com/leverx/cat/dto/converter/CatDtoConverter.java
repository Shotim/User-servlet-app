package com.leverx.cat.dto.converter;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;
import com.leverx.pet.dto.converter.PetDtoConverter;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class CatDtoConverter {

    private final PetDtoConverter converter = new PetDtoConverter();

    public Cat catInputDtoToCat(CatInputDto catInputDto) {
        var name = catInputDto.getName();
        var dateOfBirth = catInputDto.getDateOfBirth();
        int miceCaughtNumber = catInputDto.getMiceCaughtNumber();
        return new Cat(name, dateOfBirth, miceCaughtNumber);
    }

    public CatOutputDto catToCatOutputDto(Cat cat) {
        var catOutputDto = converter.petToPetOutputDto(cat, CatOutputDto.class);
        var miceCaughtNumber = cat.getMiceCaughtNumber();
        catOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        return catOutputDto;
    }

    public Collection<CatOutputDto> catCollectionToCatOutputDtoCollection(Collection<Cat> cats) {
        return cats.stream()
                .map(this::catToCatOutputDto)
                .collect(toList());
    }
}