package com.leverx.user.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MySQLDriver {

    public Connection establishConnection() {

        Logger logger = LoggerFactory.getLogger(MySQLDriver.class);
        Connection connection = null;
        try (InputStream input = MySQLDriver.class.getClassLoader().getResourceAsStream("database.properties")) {

            Properties properties = new Properties();

            if (input != null) {
                properties.load(input);
            }
            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Connection was created");

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return connection;
    }
}
