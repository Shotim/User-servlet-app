package com.leverx.user.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionPool extends ObjectPool<Connection> {
    private static final Logger logger = LoggerFactory.getLogger(DBConnectionPool.class);
    private DataBaseProperties properties = new DataBaseProperties();

    public DBConnectionPool() {
        super();
        try {
            Class.forName(properties.getDriverClassName());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public Connection create() {
        try {
            var url = properties.getDatabaseUrl();
            var user = properties.getDatabaseUsername();
            var password = properties.getDatabasePassword();
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
            return null;
        }
    }

    @Override
    public boolean validate(Connection object) {
        try {
            return !object.isClosed();
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
            return false;
        }
    }

    @Override
    public void dead(Connection object) {
        try {
            object.close();
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
        }
    }
}
