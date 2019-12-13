package com.leverx.entity.dog.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.leverx.entity.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DogOutputDto extends PetOutputDto {

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    boolean isCutEars;

    public DogOutputDto(int id, String name, LocalDate dateOfBirth, List<Integer> ownerIds, boolean isCutEars) {
        super(id, name, dateOfBirth, ownerIds);
        this.isCutEars = isCutEars;
    }

    public DogOutputDto(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
    }
}