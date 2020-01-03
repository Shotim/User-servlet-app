package com.leverx.core.config;

import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.leverx.core.loader.DBCredentialsLoaderProvider.getDBCredentialsLoader;

public class TomcatCPConfig {

    private static final String DRIVER_CLASSNAME = "com.mysql.cj.jdbc.Driver";
    private static DataSource dataSource;

    public static final int IDLE_NUMBER = 20;

    public static void createConnectionPool() {
        var dbCredentialsLoader = getDBCredentialsLoader();
        var url = dbCredentialsLoader.getUrl();
        var username = dbCredentialsLoader.getUsername();
        var password = dbCredentialsLoader.getPassword();
        var properties = new PoolProperties();
        properties.setUrl(url);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setDriverClassName(DRIVER_CLASSNAME);
        properties.setMaxIdle(IDLE_NUMBER);
        properties.setMinIdle(IDLE_NUMBER);

        dataSource = new org.apache.tomcat.jdbc.pool.DataSource(properties);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}