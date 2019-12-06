package com.leverx.user.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class UserInputDto {

    @Size(min = 1, max = 60, message = NOT_VALID_NAME)
    @NotNull
    String name;

    Collection<Integer> catsIdsList = new ArrayList<>();
}