package com.company.repository;

import com.company.driver.Driver;
import com.company.driver.MySQLDriver;
import com.company.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public UserRepositoryImpl(){
        driver = new MySQLDriver();
    }

    private Driver driver;

    @Override
    public List<User> getAll() {
        Connection connection = driver.getConnection();
        List<User> users = null;
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
//        Connection connection = driver.getConnection();
//        User user = new User();
//        try {
//            preparedStatement = connection.prepareStatement();
//            resultSet.updateString(1, Integer.toString(id));
//            resultSet = preparedStatement.executeQuery(SELECT_ONE_BY_ID);
//
//            while (resultSet.next()) {
//                user.setId(resultSet.getInt("id"));
//                user.setName(resultSet.getString("name"));
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            cleanResultSetAndStatement();
//        }
//        return user;
        return null;
    }

    @Override
    public List<User> getByName(String name) {
//        Connection connection = driver.getConnection();
//        List<User> users = null;
//        try {
//            preparedStatement = connection.createStatement();
//            resultSet.updateString(1, name);
//            resultSet = preparedStatement.executeQuery(SELECT_BY_NAME);
//
//            while (resultSet.next()) {
//                users.add(
//                        new User(resultSet.getInt("id"),
//                                resultSet.getString("name")));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            cleanResultSetAndStatement();
//        }
//        return users;
        return null;
    }

    @Override
    public void addUser(User user) {
//        Connection connection = driver.getConnection();
//        try {
//            preparedStatement = connection.createStatement();
//            resultSet.updateString(1, user.getName());
//            preparedStatement.executeUpdate(ADD_ONE);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            cleanResultSetAndStatement();
//        }
    }

    @Override
    public void deleteById(int id) {
//        Connection connection = driver.getConnection();
//        try {
//            preparedStatement = connection.createStatement();
//            resultSet.updateString(1, Integer.toString(id));
//            resultSet = preparedStatement.executeQuery(DELETE_BY_ID);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            cleanResultSetAndStatement();
//        }
    }

    @Override
    public void updateById(User user) {
//        Connection connection = driver.getConnection();
//        try {
//            preparedStatement = connection.createStatement();
//            resultSet.updateString(1, user.getName());
//            resultSet.updateString(2, Integer.toString(user.getId()));
//            preparedStatement.executeUpdate(UPDATE_BY_ID);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            cleanResultSetAndStatement();
//        }
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
