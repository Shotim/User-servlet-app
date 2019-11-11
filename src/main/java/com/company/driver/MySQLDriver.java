package com.company.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLDriver implements com.company.driver.Driver {

    public Connection establishConnection() {

        Connection connection = null;
        Logger logger = LoggerFactory.getLogger(MySQLDriver.class);
        logger.info("Connecting to database...");
        try (InputStream input = MySQLDriver.class.getClassLoader().getResourceAsStream("database.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Connected to My SQL Database");

        } catch (SQLException ex) {
            logger.error("SQL State: {}\n{}", ex.getSQLState(), ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return connection;
    }
}
