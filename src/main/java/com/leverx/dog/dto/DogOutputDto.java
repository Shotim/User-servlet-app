package com.leverx.dog.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.leverx.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DogOutputDto extends PetOutputDto {

    boolean isCutEars;

    public DogOutputDto(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
    }
}