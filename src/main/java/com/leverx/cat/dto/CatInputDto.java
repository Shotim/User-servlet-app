package com.leverx.cat.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CatInputDto {

    @NotNull
    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    String name;

    @NotNull
    @PastOrPresent
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate dateOfBirth;
}