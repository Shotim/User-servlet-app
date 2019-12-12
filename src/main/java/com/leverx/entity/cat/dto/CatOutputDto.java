package com.leverx.entity.cat.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.leverx.entity.pet.dto.PetOutputDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import static com.leverx.validator.EntityValidator.NON_NEGATIVE_NUMBER;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;

@Getter
@JsonIdentityInfo(generator = PropertyGenerator.class, property = "id")
public class CatOutputDto extends PetOutputDto {

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PositiveOrZero(message = NON_NEGATIVE_NUMBER)
    int miceCaughtNumber;

    public CatOutputDto(int id, String name, LocalDate dateOfBirth, List<Integer> ownerIds, int miceCaughtNumber) {
        super(id, name, dateOfBirth, ownerIds);
        this.miceCaughtNumber = miceCaughtNumber;
    }
}