package com.leverx.user.repository;

import com.leverx.database.DBConnectionPool;
import com.leverx.user.entity.User;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import static com.leverx.user.repository.SQLQuery.ADD_ONE_USER;
import static com.leverx.user.repository.SQLQuery.DELETE_USER_BY_ID;
import static com.leverx.user.repository.SQLQuery.SELECT_ALL_USERS;
import static com.leverx.user.repository.SQLQuery.SELECT_ONE_USER_BY_ID;
import static com.leverx.user.repository.SQLQuery.SELECT_USER_BY_NAME;
import static com.leverx.user.repository.SQLQuery.UPDATE_USER_BY_ID;
import static com.leverx.utils.RepositoryUtils.extractFirstUserFromResultSet;
import static com.leverx.utils.RepositoryUtils.extractUsersFromResultSet;
import static org.slf4j.LoggerFactory.getLogger;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = getLogger(UserRepositoryImpl.class);
    private final DBConnectionPool connectionPool;

    public UserRepositoryImpl() {
        connectionPool = DBConnectionPool.getInstance();
    }

    @Override
    public Collection<User> findAll() {
        Connection connection = connectionPool.takeConnection();
        LOGGER.debug("Connection created");

        try (var preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
             var resultSet = preparedStatement.executeQuery()) {

            var users = extractUsersFromResultSet(resultSet);
            LOGGER.debug("Found {} users", users.size());
            return users;
        } catch (SQLException ex) {
            LOGGER.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException(ex);

        } finally {
            connectionPool.putConnection(connection);
        }
    }

    @Override
    public User findById(int id) {
        Connection connection = connectionPool.takeConnection();
        LOGGER.debug("Connection created");

        try (var preparedStatement = connection.prepareStatement(SELECT_ONE_USER_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {

                var user = extractFirstUserFromResultSet(resultSet);
                LOGGER.debug("User from database with id = {} was received", user.getId());
                return user;
            }

        } catch (SQLException ex) {
            LOGGER.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException(ex);

        } finally {
            connectionPool.putConnection(connection);
        }
    }

    @Override
    public Collection<User> findByName(String name) {
        Connection connection = connectionPool.takeConnection();
        LOGGER.debug("Connection created");

        try (var preparedStatement = connection.prepareStatement(SELECT_USER_BY_NAME)) {

            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {

                var users = extractUsersFromResultSet(resultSet);
                LOGGER.debug("{} users from database with name = {} were received",
                        users.size(), name);
                return users;
            }

        } catch (SQLException ex) {
            LOGGER.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException(ex);

        } finally {
            connectionPool.putConnection(connection);
        }
    }

    @Override
    public User save(User user) {
        Connection connection = connectionPool.takeConnection();
        LOGGER.debug("Connection created");

        try (var preparedStatement = connection.prepareStatement(ADD_ONE_USER)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.executeUpdate();
            LOGGER.debug("User with name = {} was added to database", user.getName());
            return user;
        } catch (SQLException ex) {
            LOGGER.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException(ex);

        } finally {
            connectionPool.putConnection(connection);
        }
    }

    @Override
    public void deleteById(String id) {
        Connection connection = connectionPool.takeConnection();
        LOGGER.debug("Connection created");

        try (var preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {

            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            LOGGER.debug("User from database with id = {} was deleted", id);
        } catch (SQLException ex) {
            LOGGER.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException(ex);

        } finally {
            connectionPool.putConnection(connection);
        }
    }

    @Override
    public User updateById(User user) {
        Connection connection = connectionPool.takeConnection();
        LOGGER.debug("Connection created");

        try (var preparedStatement = connection.prepareStatement(UPDATE_USER_BY_ID)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            LOGGER.debug("User with id = {} in database was updated", user.getId());
            return user;
        } catch (SQLException ex) {
            LOGGER.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException(ex);

        } finally {
            connectionPool.putConnection(connection);
        }
    }
}
