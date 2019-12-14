package com.leverx.model.cat.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.leverx.model.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import static com.leverx.validator.EntityValidator.NON_NEGATIVE_NUMBER;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;

@Getter
@Setter
@JsonIdentityInfo(generator = PropertyGenerator.class, property = "id")
public class CatOutputDto extends PetOutputDto {

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PositiveOrZero(message = NON_NEGATIVE_NUMBER)
    int miceCaughtNumber;

    public CatOutputDto(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
    }
}