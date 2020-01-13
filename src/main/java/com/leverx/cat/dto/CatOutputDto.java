package com.leverx.cat.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.leverx.pet.dto.PetOutputDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatOutputDto that = (CatOutputDto) o;
        return (miceCaughtNumber == that.miceCaughtNumber
                && ((CatOutputDto) o).getDateOfBirth().isEqual(that.getDateOfBirth())
                && ((CatOutputDto) o).getId() == that.getId()
                && ((CatOutputDto) o).getName().equals(that.getName())
                && ((CatOutputDto) o).getOwnerIds().equals(that.getOwnerIds()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(miceCaughtNumber);
    }
}