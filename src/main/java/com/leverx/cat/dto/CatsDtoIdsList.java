package com.leverx.cat.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CatsDtoIdsList {
    List<Integer> ids;
}
