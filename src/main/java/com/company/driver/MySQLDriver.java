package com.company.driver;

import com.mysql.jdbc.Driver;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.Thread.currentThread;
import static java.sql.DriverManager.getConnection;
import static java.sql.DriverManager.registerDriver;

public class MySQLDriver {

    public Connection establishConnection() {

        Properties properties = new Properties();
        Connection connection = null;

        try {
            ClassLoader loader = currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream("/resources/db.properties");
            properties.load(input);

            String url = properties.getProperty("url");

            connection = getConnection(url, properties);
            registerDriver(new Driver());

        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
