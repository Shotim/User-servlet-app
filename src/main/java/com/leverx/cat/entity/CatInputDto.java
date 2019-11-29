package com.leverx.cat.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CatInputDto {

    @Size(min = 5, max = 60, message = NOT_VALID_NAME)
    String name;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    Date dateOfBirth;
}