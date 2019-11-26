package com.leverx.cat.entity;


import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.sql.Date;

import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CatDto {

    @Size(min = 5, max = 60, message = NOT_VALID_NAME)
    String name;

    Date dateOfBirth;
}
