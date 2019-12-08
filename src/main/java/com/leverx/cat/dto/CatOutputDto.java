package com.leverx.cat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.leverx.user.dto.UserOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;

@Getter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CatOutputDto {

    int id;

    @NotNull
    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    String name;

    @NotNull
    @PastOrPresent
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @JsonInclude(NON_NULL)
    UserOutputDto owner;
}
