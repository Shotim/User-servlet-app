package com.leverx.user.repository;

import com.leverx.user.driver.DBConnectionPool;
import com.leverx.user.driver.ObjectPool;
import com.leverx.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.leverx.user.entity.User.ID;
import static com.leverx.user.entity.User.NAME;

public class UserRepositoryImpl implements UserRepository {

    private static final int FIRST_QUERY_ARGUMENT = 1;
    private static final int SECOND_QUERY_ARGUMENT = 2;

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private ObjectPool<Connection> connectionPool;

    public UserRepositoryImpl() {
        connectionPool = new DBConnectionPool();
    }

    @Override
    public List<User> findAll() {

        Connection connection = connectionPool.takeOut();
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();

            users = extractUsersFromResultSet(resultSet);
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        connectionPool.takeIn(connection);
        return users;
    }

    @Override
    public User findById(int id) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ONE_USER_BY_ID);
            preparedStatement.setInt(FIRST_QUERY_ARGUMENT, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = extractUsersFromResultSet(resultSet).get(0);
            return user;
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        connectionPool.takeIn(connection);
        return null;
    }

    @Override
    public List<User> findByName(String name) {
        Connection connection = connectionPool.takeOut();
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_USER_BY_NAME);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            users = extractUsersFromResultSet(resultSet);
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        connectionPool.takeIn(connection);
        return users;
    }

    @Override
    public void save(User user) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.ADD_ONE_USER);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, user.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        connectionPool.takeIn(connection);
    }

    @Override
    public void deleteById(String id) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.DELETE_USER_BY_ID);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        connectionPool.takeIn(connection);
    }

    @Override
    public void updateById(String id, User user) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.UPDATE_USER_BY_ID);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, user.getName());
            preparedStatement.setString(SECOND_QUERY_ARGUMENT, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
        }
        connectionPool.takeIn(connection);
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
