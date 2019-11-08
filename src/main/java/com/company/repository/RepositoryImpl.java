package com.company.repository;

import com.company.driver.MySQLDriver;
import com.company.entity.User;
import lombok.Setter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class RepositoryImpl implements Repository {

    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_ONE_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String ADD_USER = "INSERT INTO users(name) VALUES (?) ";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

    private Statement statement;
    private ResultSet resultSet;
    @Setter
    private MySQLDriver driver;


    @Override
    public List<User> getAll() throws SQLException {
        Connection connection = driver.establishConnection();
        List<User> users = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_USERS);

            while (resultSet.next()) {
                users.add(
                        new User(resultSet.getInt("id"),
                                resultSet.getString("name")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }

        return users;
    }

    @Override
    public User getById(int id) {
        Connection connection = driver.establishConnection();
        User user = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ONE_BY_ID);

            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        Connection connection = driver.establishConnection();
        try {
            statement = connection.createStatement();
            resultSet.updateString(1, user.getName());
            statement.executeUpdate(ADD_USER);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }
    }

    @Override
    public void deleteById(int id) {
        Connection connection = driver.establishConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(DELETE_USER_BY_ID);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }
    }

    private void cleanResultSetAndStatement() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            resultSet = null;
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            statement = null;
        }
    }
}
