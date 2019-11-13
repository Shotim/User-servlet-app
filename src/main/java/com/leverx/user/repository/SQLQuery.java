package com.leverx.user.repository;

public class SQLQuery {
    public static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String SELECT_ONE_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ?";
    public static final String ADD_ONE_USER = "INSERT INTO users(name) VALUES (?) ";
    public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    public static final String UPDATE_USER_BY_ID = "UPDATE users SET name = ? WHERE id = ?";
}
