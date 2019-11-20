package com.leverx.database;

import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.sql.DriverManager.getConnection;
import static java.util.stream.Stream.generate;
import static org.slf4j.LoggerFactory.getLogger;

public class DBConnectionPool {

    private static final int MAX_POOL_CONNECTION_AMOUNT = 10;
    private static final Logger LOGGER = getLogger(DBConnectionPool.class);
    private static DBPropertyLoader propertyLoader;
    private static DBConnectionPool connectionPool;
    private BlockingQueue<Connection> connectionOutOfUsage = new LinkedBlockingQueue<>(MAX_POOL_CONNECTION_AMOUNT);

    private DBConnectionPool() {
        propertyLoader = new DBPropertyLoader();
        addDriver();
        generate(DBConnectionPool::createConnection)
                .limit(MAX_POOL_CONNECTION_AMOUNT)
                .forEach(connectionOutOfUsage::add);
        LOGGER.debug("DBConnectionPool instance was created");
    }

    public static synchronized DBConnectionPool getInstance() {
        if (connectionPool == null) {
            connectionPool = new DBConnectionPool();
        }
        return connectionPool;
    }

    private static Connection createConnection() {
        try {
            var url = propertyLoader.getUrl();
            var user = propertyLoader.getUsername();
            var password = propertyLoader.getPassword();

            var connection = getConnection(url, user, password);
            LOGGER.info("Connection created");
            return connection;

        } catch (SQLException e) {
            LOGGER.error("Can't create connection to jdbc Driver. Credentials are wrong");
            throw new InternalServerErrorException(e);
        }
    }

    private void addDriver() {
        try {
            var driver = propertyLoader.getDriver();
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    public Connection takeConnection() {
        try {
            Connection connection = connectionOutOfUsage.take();
            LOGGER.debug("Connection was received from pool");
            return connection;
        } catch (InterruptedException e) {
            LOGGER.debug(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    public void putConnection(Connection connection) {
        try {
            connectionOutOfUsage.put(connection);
            LOGGER.debug("Connection was returned to pool");
        } catch (InterruptedException e) {
            LOGGER.debug(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }
}
