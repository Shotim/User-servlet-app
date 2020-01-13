package com.leverx.dog.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import com.leverx.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@JsonIdentityInfo(generator = PropertyGenerator.class, property = "id")
public class DogOutputDto extends PetOutputDto {

    boolean isCutEars;

    public DogOutputDto(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogOutputDto that = (DogOutputDto) o;
        return (isCutEars == that.isCutEars
                && ((DogOutputDto) o).getDateOfBirth().isEqual(that.getDateOfBirth())
                && ((DogOutputDto) o).getId() == that.getId()
                && ((DogOutputDto) o).getName().equals(that.getName())
                && ((DogOutputDto) o).getOwnerIds().equals(that.getOwnerIds()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCutEars);
    }
}