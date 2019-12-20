package com.leverx.user.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class UserInputDto {

    @Size(min = MIN_SIZE, max = MAX_SIZE)
    @NotNull
    String name;

    Collection<Integer> catsIds = emptyList();

    Collection<Integer> dogsIds = emptyList();
}