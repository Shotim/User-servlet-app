package com.leverx.user.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.leverx.cat.dto.CatOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;
import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserOutputDto {

    int id;

    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    String name;

    @JsonInclude(NON_NULL)
    Collection<CatOutputDto> cats = emptyList();

}
