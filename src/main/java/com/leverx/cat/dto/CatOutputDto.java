package com.leverx.cat.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.leverx.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

@Getter
@Setter
@JsonIdentityInfo(generator = PropertyGenerator.class, property = "id")
public class CatOutputDto extends PetOutputDto {

    @PositiveOrZero
    int miceCaughtNumber;

    public CatOutputDto(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
    }
}