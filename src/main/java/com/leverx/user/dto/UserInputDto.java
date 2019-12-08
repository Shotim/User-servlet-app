package com.leverx.user.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class UserInputDto {

    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    @NotNull
    String name;

    Collection<Integer> catsIds = new ArrayList<>();
}
