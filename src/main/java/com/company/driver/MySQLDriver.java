package com.company.driver;

import com.mysql.jdbc.Driver;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;
import static java.sql.DriverManager.registerDriver;

public class MySQLDriver implements com.company.driver.Driver {

    private static Connection connection;

    public Connection establishConnection() {
        try (InputStream input = MySQLDriver.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties properties = new Properties();

            if (input != null) {
                properties.load(input);
            }

            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            connection = getConnection(url, user, password);
            registerDriver(new Driver());

        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
