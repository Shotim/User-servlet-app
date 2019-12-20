package com.leverx.cat.dto.converter;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;

import java.util.Collection;

import static com.leverx.pet.dto.converter.PetDtoConverter.petToPetOutputDto;
import static java.util.stream.Collectors.toList;

public class CatDtoConverter {

    public static Cat catInputDtoToCat(CatInputDto catInputDto) {
        var name = catInputDto.getName();
        var dateOfBirth = catInputDto.getDateOfBirth();
        int miceCaughtNumber = catInputDto.getMiceCaughtNumber();
        return new Cat(name, dateOfBirth, miceCaughtNumber);
    }

    public static CatOutputDto catToCatOutputDto(Cat cat) {
        var catOutputDto = petToPetOutputDto(cat, CatOutputDto.class);
        var miceCaughtNumber = cat.getMiceCaughtNumber();
        catOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        return catOutputDto;
    }

    public static Collection<CatOutputDto> catCollectionToCatOutputDtoCollection(Collection<Cat> cats) {
        return cats.stream()
                .map(CatDtoConverter::catToCatOutputDto)
                .collect(toList());
    }
}