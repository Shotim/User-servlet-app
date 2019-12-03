package com.leverx.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserInputDto {

    @NotNull
    @Size(min = 1, max = 60, message = NOT_VALID_NAME)
    String name;
}
