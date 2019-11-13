package com.leverx.user.repository;

import com.leverx.database.DBConnectionPool;
import com.leverx.objectpool.ObjectPool;
import com.leverx.user.DTOUser;
import com.leverx.user.entity.User;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.leverx.constants.SQLQuery.ADD_ONE_USER;
import static com.leverx.constants.SQLQuery.DELETE_USER_BY_ID;
import static com.leverx.constants.SQLQuery.SELECT_ALL_USERS;
import static com.leverx.constants.SQLQuery.SELECT_ONE_USER_BY_ID;
import static com.leverx.constants.SQLQuery.SELECT_USER_BY_NAME;
import static com.leverx.constants.SQLQuery.UPDATE_USER_BY_ID;
import static com.leverx.constants.UserConstants.ID;
import static com.leverx.constants.UserConstants.NAME;
import static org.slf4j.LoggerFactory.getLogger;

public class UserRepositoryImpl implements UserRepository {

    private static final int FIRST_QUERY_ARGUMENT = 1;
    private static final int SECOND_QUERY_ARGUMENT = 2;

    private static final Logger logger = getLogger(UserRepositoryImpl.class);
    private ObjectPool<Connection> connectionPool;

    public UserRepositoryImpl() {
        connectionPool = new DBConnectionPool();
    }

    @Override
    public Collection<User> findAll() {

        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();

            Collection<User> users = new ArrayList<>();
            users = extractUsersFromResultSet(resultSet);
            logger.info("Objects from database were received");
            return users;
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            connectionPool.takeIn(connection);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public User findById(int id) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_USER_BY_ID);
            preparedStatement.setInt(FIRST_QUERY_ARGUMENT, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = extractUsersFromResultSet(resultSet).get(0);
            connectionPool.takeIn(connection);
            logger.info("Object from database with specific id was received");
            return user;
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            connectionPool.takeIn(connection);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public Collection<User> findByName(String name) {
        Connection connection = connectionPool.takeOut();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_NAME);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            users = extractUsersFromResultSet(resultSet);
            connectionPool.takeIn(connection);
            logger.info("Objects from database with specific name were received");
            return users;
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            connectionPool.takeIn(connection);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void save(DTOUser user) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_ONE_USER);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, user.getName());
            preparedStatement.executeUpdate();
            connectionPool.takeIn(connection);
            logger.info("Object was added to database");
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            connectionPool.takeIn(connection);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void deleteById(String id) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, id);
            preparedStatement.executeUpdate();
            connectionPool.takeIn(connection);
            logger.info("Object from database was deleted");
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            connectionPool.takeIn(connection);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void updateById(String id, DTOUser user) {
        Connection connection = connectionPool.takeOut();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_BY_ID);
            preparedStatement.setString(FIRST_QUERY_ARGUMENT, user.getName());
            preparedStatement.setString(SECOND_QUERY_ARGUMENT, id);
            preparedStatement.executeUpdate();
            connectionPool.takeIn(connection);
            logger.info("Object in database was updated");
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            connectionPool.takeIn(connection);
            throw new InternalServerErrorException();
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

    private User extractFirstUserFromResultSet(ResultSet resultSet) throws SQLException {
        return extractUsersFromResultSet(resultSet).get(0);
    }
}
