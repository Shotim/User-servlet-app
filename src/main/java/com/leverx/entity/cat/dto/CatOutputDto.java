package com.leverx.entity.cat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NON_NEGATIVE_NUMBER;
import static com.leverx.validator.EntityValidator.NOT_VALID_DATE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;

@Getter
@AllArgsConstructor
@JsonIdentityInfo(generator = PropertyGenerator.class, property = "id")
public class CatOutputDto {

    int id;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    String name;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PastOrPresent(message = NOT_VALID_DATE)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PositiveOrZero(message = NON_NEGATIVE_NUMBER)
    int miceCachedNumber;

    List<Integer> ownerIds;
}
