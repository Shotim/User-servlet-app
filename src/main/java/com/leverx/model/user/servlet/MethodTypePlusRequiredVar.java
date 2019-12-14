package com.leverx.model.user.servlet;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class MethodTypePlusRequiredVar<MethodType, PathVar> {
    MethodType methodType;
    PathVar pathVar;

    public void setValues(MethodType methodType, PathVar pathVar) {
        this.methodType = methodType;
        this.pathVar = pathVar;
    }
}
