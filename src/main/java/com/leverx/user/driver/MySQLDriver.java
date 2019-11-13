package com.leverx.user.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDriver {

    private static final Logger logger = LoggerFactory.getLogger(MySQLDriver.class);
    private DataBaseProperties properties = new DataBaseProperties();

    public Connection establishConnection() {

        try {
            Connection connection = null;

            var url = properties.getDATABASE_URL();
            var user = properties.getDATABASE_USERNAME();
            var password = properties.getDATABASE_PASSWORD();
            var driver = properties.getDRIVER_CLASS_NAME();

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Connection was created");
            return connection;
        } catch (ClassNotFoundException ex) {
            logger.error(ex.getMessage());
        } catch (SQLException ex) {
            logger.error("SQL state: {}\n{}", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }
}
