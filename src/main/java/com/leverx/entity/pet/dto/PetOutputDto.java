package com.leverx.entity.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NOT_VALID_DATE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;

@Getter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PetOutputDto {

    int id;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    String name;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PastOrPresent(message = NOT_VALID_DATE)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    List<Integer> ownerIds;
}
