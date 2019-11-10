package com.company.driver;

import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.DriverManager.registerDriver;

public class MySQLDriver implements com.company.driver.Driver {

    private static Connection connection;

    public Connection getConnection() {

        Logger logger = LoggerFactory.getLogger(MySQLDriver.class);
        logger.info("Connecting to database...");
        try (InputStream input = MySQLDriver.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties properties = new Properties();

            if (input != null) {
                properties.load(input);
            }

            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            connection = DriverManager.getConnection(url, user, password);
            logger.info("Connected to My SQL Database");
            registerDriver(new Driver());

        } catch (SQLException ex) {
            logger.error("SQL State: {}\n{}", ex.getSQLState(), ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
