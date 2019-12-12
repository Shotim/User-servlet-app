package com.leverx.entity.cat.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NON_NEGATIVE_NUMBER;
import static com.leverx.validator.EntityValidator.NOT_VALID_DATE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CatInputDto {

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    String name;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PastOrPresent(message = NOT_VALID_DATE)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate dateOfBirth;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PositiveOrZero(message = NON_NEGATIVE_NUMBER)
    int miceCaughtNumber;
}