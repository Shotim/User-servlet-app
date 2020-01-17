package com.leverx.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Collection;

import static com.leverx.core.validator.EntityValidator.MAX_SIZE;
import static com.leverx.core.validator.EntityValidator.MIN_SIZE;
import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserInputDto {

    @Size(min = MIN_SIZE, max = MAX_SIZE)
    @NotNull
    String name;

    @Email
    String email;

    @PositiveOrZero
    int animalPoints;

    Collection<Integer> catsIds = emptyList();

    Collection<Integer> dogsIds = emptyList();
}