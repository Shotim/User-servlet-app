package com.leverx.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

import static com.leverx.credentialsLoader.DBCredentialsLoaderProvider.getDBCredentialsLoader;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class HikDataSourceConfig {

    private static final int MAX_POOL_SIZE = 10;
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        var dbCredentialsLoader = getDBCredentialsLoader();
        var dbUrl = dbCredentialsLoader.getDBUrl();
        var username = dbCredentialsLoader.getUsername();
        var password = dbCredentialsLoader.getPassword();
        config.setJdbcUrl(dbUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}