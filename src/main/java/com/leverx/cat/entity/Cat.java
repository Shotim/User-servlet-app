package com.leverx.cat.entity;

import com.leverx.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
public class Cat {
    User owner;
    int id;
    String name;
}
