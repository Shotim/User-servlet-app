package com.leverx.database;

import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.sql.DriverManager.getConnection;
import static java.util.Collections.synchronizedList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class DBConnectionPool {

    private static final int LIST_FIRST_ELEMENT = 0;
    private static final int MAX_POOL_CONNECTION_AMOUNT = 10;
    private static final Logger logger = getLogger(DBConnectionPool.class);
    private static DataBaseProperties properties = new DataBaseProperties();
    private List<Connection> connectionInUse;
    private List<Connection> connectionOutOfUsage;

    public DBConnectionPool() {
        connectionInUse = synchronizedList(new ArrayList<Connection>());
        connectionOutOfUsage = synchronizedList(
                Stream
                        .generate(DBConnectionPool::createConnection)
                        .limit(MAX_POOL_CONNECTION_AMOUNT)
                        .collect(toList()));
        logger.debug("DBConnectionPool instance was created");
    }

    private static Connection createConnection() {
        try {
            Class.forName(properties.getDriverClassName());
            var url = properties.getDatabaseUrl();
            var user = properties.getDatabaseUsername();
            var password = properties.getDatabasePassword();

            var connection = getConnection(url, user, password);
            logger.info("Connection created");
            return connection;

        } catch (SQLException e) {
            logger.error("Can't create connection to jdbc Driver. Credentials are wrong");
            throw new InternalServerErrorException();
        } catch (ClassNotFoundException e) {
            logger.error("Can't find path to properties file");
            throw new InternalServerErrorException();
        }
    }

    public Connection startSession() {
        while (connectionOutOfUsage.isEmpty()) {
            logger.info("Wait for connection");
        }
        var connection = connectionOutOfUsage.get(LIST_FIRST_ELEMENT);
        connectionInUse.add(connection);
        logger.debug("Connection was received from pool");
        return connection;
    }

    public void finishSession(Connection connection) {
        connectionInUse.remove(connection);
        connectionOutOfUsage.add(connection);
        logger.debug("Connection was returned to pool");
    }
}