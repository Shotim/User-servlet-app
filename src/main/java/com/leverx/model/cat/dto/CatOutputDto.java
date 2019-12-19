package com.leverx.model.cat.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.leverx.model.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

@Getter
@Setter
@JsonIdentityInfo(generator = PropertyGenerator.class, property = "id")
public class CatOutputDto extends PetOutputDto {

    @NotNull
    @PositiveOrZero
    int miceCaughtNumber;

    public CatOutputDto(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
    }
}