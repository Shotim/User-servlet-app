package com.leverx.user.repository;

import com.leverx.database.DBConnectionPool;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserDto;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.leverx.constants.UserFields.ID;
import static com.leverx.constants.UserFields.NAME;
import static com.leverx.user.repository.SQLQuery.ADD_ONE_USER;
import static com.leverx.user.repository.SQLQuery.DELETE_USER_BY_ID;
import static com.leverx.user.repository.SQLQuery.SELECT_ALL_USERS;
import static com.leverx.user.repository.SQLQuery.SELECT_ONE_USER_BY_ID;
import static com.leverx.user.repository.SQLQuery.SELECT_USER_BY_NAME;
import static com.leverx.user.repository.SQLQuery.UPDATE_USER_BY_ID;
import static org.slf4j.LoggerFactory.getLogger;

//TODO change UserDto to User
public class UserRepositoryImpl implements UserRepository {

    private static final Logger logger = getLogger(UserRepositoryImpl.class);
    private static final int FIRST = 0;
    private final DBConnectionPool connectionPool;

    public UserRepositoryImpl() {
        connectionPool = new DBConnectionPool();
    }

    @Override
    public Collection<User> findAll() {
        Connection connection = establishConnection();

        try (var preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
             var resultSet = preparedStatement.executeQuery()) {

            var users = extractUsersFromResultSet(resultSet);
            logger.debug("Found {} users", users.size());
            return users;
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException();

        } finally {
            connectionPool.finishSession(connection);
        }
    }

    @Override
    public User findById(int id) {
        Connection connection = establishConnection();

        try (var preparedStatement = connection.prepareStatement(SELECT_ONE_USER_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {

                var user = extractFirstUserFromResultSet(resultSet);
                logger.debug("User from database with id = {} was received", user.getId());
                return user;
            }

        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException();

        } finally {
            connectionPool.finishSession(connection);
        }
    }

    @Override
    public Collection<User> findByName(String name) {
        Connection connection = establishConnection();

        try (var preparedStatement = connection.prepareStatement(SELECT_USER_BY_NAME)) {

            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {

                var users = extractUsersFromResultSet(resultSet);
                logger.debug("{} users from database with name = {} were received",
                        users.size(), users.get(FIRST).getName());
                return users;
            }

        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException();

        } finally {
            connectionPool.finishSession(connection);
        }
    }

    @Override
    public void save(UserDto user) {
        Connection connection = establishConnection();

        try (var preparedStatement = connection.prepareStatement(ADD_ONE_USER)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.executeUpdate();
            logger.debug("User with name = {} was added to database", user.getName());
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException();

        } finally {
            connectionPool.finishSession(connection);
        }
    }

    @Override
    public void deleteById(String id) {
        Connection connection = establishConnection();

        try (var preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {

            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            logger.debug("User from database with id = {} was deleted", id);
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException();

        } finally {
            connectionPool.finishSession(connection);
        }
    }

    @Override
    public void updateById(String id, UserDto user) {
        Connection connection = establishConnection();

        try (var preparedStatement = connection.prepareStatement(UPDATE_USER_BY_ID)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            logger.debug("User with id = {} in database was updated", id);
        } catch (SQLException ex) {
            logger.error("SQL state:{}\n{}", ex.getSQLState(), ex.getMessage());
            throw new InternalServerErrorException();

        } finally {
            connectionPool.finishSession(connection);
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
        return extractUsersFromResultSet(resultSet).get(FIRST);
    }

    private Connection establishConnection() {
        Connection connection = connectionPool.startSession();
        logger.debug("Connection created");
        return connection;
    }

}
