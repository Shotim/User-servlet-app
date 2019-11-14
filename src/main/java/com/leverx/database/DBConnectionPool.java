package com.leverx.database;

import com.leverx.objectpool.ObjectPool;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;
import static org.slf4j.LoggerFactory.getLogger;

public class DBConnectionPool extends ObjectPool<Connection> {
    private static final Logger logger = getLogger(DBConnectionPool.class);
    private DataBaseProperties properties = new DataBaseProperties();

    public DBConnectionPool() {
        super();
        try {
            Class.forName(properties.getDriverClassName());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        logger.info("DataBase connection pool was created");
    }

    @Override
    public Connection create() {
        try {
            var url = properties.getDatabaseUrl();
            var user = properties.getDatabaseUsername();
            var password = properties.getDatabasePassword();
            var connection = getConnection(url, user, password);
            logger.info("Connection created");
            return connection;
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
            return null;
        }
    }

    @Override
    public boolean validate(Connection object) {
        try {
            boolean isValid = !object.isClosed();
            logger.info("Connection was validated");
            return isValid;
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
            return false;
        }
    }

    @Override
    public void dead(Connection object) {
        try {
            object.close();
            logger.info("Connection was closed");
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
        }
    }
}
