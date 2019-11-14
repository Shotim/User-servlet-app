package com.leverx.user.repository;

import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PACKAGE;

@FieldDefaults(level = PACKAGE, makeFinal = true)
class SQLQuery {
    static String SELECT_ALL_USERS = "SELECT * FROM users";
    static String SELECT_ONE_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    static String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ?";
    static String ADD_ONE_USER = "INSERT INTO users(name) VALUES (?) ";
    static String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    static String UPDATE_USER_BY_ID = "UPDATE users SET name = ? WHERE id = ?";
}
