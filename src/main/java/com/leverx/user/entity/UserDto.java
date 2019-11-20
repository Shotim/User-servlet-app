package com.leverx.user.entity;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class UserDto {

    @Size(min = 5, max = 60, message = "Name should be between 5 and 60 symbols")
    @NotNull
    String name;
}
