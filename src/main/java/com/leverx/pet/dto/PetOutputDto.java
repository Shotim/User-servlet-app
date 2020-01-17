package com.leverx.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.leverx.core.validator.EntityValidator.MAX_SIZE;
import static com.leverx.core.validator.EntityValidator.MIN_SIZE;

@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PetOutputDto {

    int id;

    @NotNull
    @Size(min = MIN_SIZE, max = MAX_SIZE)
    String name;

    @NotNull
    @PastOrPresent
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    List<Integer> ownerIds = new ArrayList<>();

    public PetOutputDto(int id, String name, LocalDate dateOfBirth) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetOutputDto that = (PetOutputDto) o;
        return (((PetOutputDto) o).getDateOfBirth().isEqual(that.getDateOfBirth())
                && ((PetOutputDto) o).getId() == that.getId()
                && ((PetOutputDto) o).getName().equals(that.getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, ownerIds);
    }
}
