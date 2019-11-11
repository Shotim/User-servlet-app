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
        try (InputStream input = MySQLDriver.class.getClassLoader().getResourceAsStream("database.properties")) {

            Properties properties = new Properties();

            properties.load(input);

            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return connection;
    }
}
