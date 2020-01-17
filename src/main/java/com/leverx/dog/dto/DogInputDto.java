package com.leverx.dog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.leverx.core.validator.EntityValidator.MAX_SIZE;
import static com.leverx.core.validator.EntityValidator.MIN_SIZE;
import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class DogInputDto {

    @NotNull
    @Size(min = MIN_SIZE, max = MAX_SIZE)
    String name;

    @NotNull
    @PastOrPresent
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate dateOfBirth;

    boolean isCutEars;
}
