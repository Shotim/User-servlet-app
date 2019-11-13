package com.company.repository;

import com.company.driver.MySQLDriver;
import com.company.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final int FIRST_QUERY_ARGUMENT = 1;
    private static final int SECOND_QUERY_ARGUMENT = 2;
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MySQLDriver driver;

    public UserRepositoryImpl() {
        driver = new MySQLDriver();
    }

    @Override
    public List<User> getAll() {

        Connection connection = driver.establishConnection();
        List<User> users = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_USERS);
            resultSet = preparedStatement.executeQuery();

            users = extractUsersFromResultSet(resultSet);
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        return users;
    }

    @Override
    public User getById(int id) {
        Connection connection = driver.establishConnection();
        User user = new User();
        try {
            preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ONE_USER_BY_ID);
            preparedStatement.setInt(FIRST_QUERY_ARGUMENT, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user.setId(resultSet.getInt(ID));
                user.setName(resultSet.getString(NAME));
            }
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getByName(String name) {
        Connection connection = driver.establishConnection();
        List<User> users = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(SQLQuery.SELECT_USER_BY_NAME);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, name);
            resultSet = preparedStatement.executeQuery();

            users = extractUsersFromResultSet(resultSet);
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        return users;
    }

    @Override
    public void save(User user) {
        Connection connection = driver.establishConnection();
        try {
            preparedStatement = connection.prepareStatement(SQLQuery.ADD_ONE_USER);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, user.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        Connection connection = driver.establishConnection();
        try {
            preparedStatement = connection.prepareStatement(SQLQuery.DELETE_USER_BY_ID);
            preparedStatement.setInt(FIRST_QUERY_ARGUMENT, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
    }

    @Override
    public void updateById(String id, User user) {
        Connection connection = driver.establishConnection();
        try {
            preparedStatement = connection.prepareStatement(SQLQuery.UPDATE_USER_BY_ID);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, user.getName());
            preparedStatement.setString(SECOND_QUERY_ARGUMENT, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
    }

    private List<User> extractUsersFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            users.add(
                    new User(resultSet.getInt(ID),
                            resultSet.getString(NAME)));
        }
        return users;
    }

}
