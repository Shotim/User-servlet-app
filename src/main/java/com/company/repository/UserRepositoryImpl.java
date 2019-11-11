package com.company.repository;

import com.company.driver.Driver;
import com.company.driver.MySQLDriver;
import com.company.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final String SELECT_ALL = "SELECT * FROM users";
    private static final String SELECT_ONE_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM users WHERE name = ?";
    private static final String ADD_ONE = "INSERT INTO users(name) VALUES (?) ";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_BY_ID = "UPDATE users SET name = ? WHERE id = ?";

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Driver driver;

    public UserRepositoryImpl() {
        driver = new MySQLDriver();
    }

    @Override
    public List<User> getAll() {

        Connection connection = driver.establishConnection();
        List<User> users = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL);
            resultSet = preparedStatement.executeQuery();

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
        User user = new User();
        try {
            preparedStatement = connection.prepareStatement(SELECT_ONE_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

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
    public List<User> getByName(String name) {
        Connection connection = driver.establishConnection();
        List<User> users = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(SELECT_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(
                        new User(resultSet.getInt("id"),
                                resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        Connection connection = driver.establishConnection();
        try {
            preparedStatement = connection.prepareStatement(ADD_ONE);
            preparedStatement.setString(1, user.getName());
            preparedStatement.executeUpdate();
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
            preparedStatement = connection.prepareStatement(DELETE_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }
    }

    @Override
    public void updateById(String id, User user) {
        Connection connection = driver.establishConnection();
        try {
            preparedStatement = connection.prepareStatement(UPDATE_BY_ID);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
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
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            preparedStatement = null;
        }
    }
}
