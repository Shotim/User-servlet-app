package com.leverx.user.servlet;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class Pair<L, R> {
    L left;
    R right;

    public void setValues(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
