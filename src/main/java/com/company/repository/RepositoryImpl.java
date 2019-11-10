package com.company.repository;

import com.company.driver.MySQLDriver;
import com.company.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class RepositoryImpl implements Repository {

    private static final String SELECT_ALL = "SELECT * FROM users";
    private static final String SELECT_ONE_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM users WHERE name = ?";
    private static final String ADD_ONE = "INSERT INTO users(name) VALUES (?) ";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_BY_ID = "UPDATE users SET name = ? WHERE id = ?";

    private Statement statement;
    private ResultSet resultSet;

    private MySQLDriver driver;

    public RepositoryImpl(MySQLDriver driver) {
        this.driver = driver;
    }


    @Override
    public List<User> getAll() {
        Connection connection = driver.establishConnection();
        List<User> users = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL);

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
            statement = connection.createStatement();
            resultSet.updateString(1, Integer.toString(id));
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
    public List<User> getByName(String name) {
        Connection connection = driver.establishConnection();
        List<User> users = null;
        try {
            statement = connection.createStatement();
            resultSet.updateString(1, name);
            resultSet = statement.executeQuery(SELECT_BY_NAME);

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
            statement = connection.createStatement();
            resultSet.updateString(1, user.getName());
            statement.executeUpdate(ADD_ONE);
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
            resultSet.updateString(1, Integer.toString(id));
            resultSet = statement.executeQuery(DELETE_BY_ID);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanResultSetAndStatement();
        }
    }

    @Override
    public void updateById(User user) {
        Connection connection = driver.establishConnection();
        try {
            statement = connection.createStatement();
            resultSet.updateString(1, user.getName());
            resultSet.updateString(2, Integer.toString(user.getId()));
            statement.executeUpdate(UPDATE_BY_ID);
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
