package com.company.driver;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MySQLDriver {

    public Connection establishConnection() {

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
