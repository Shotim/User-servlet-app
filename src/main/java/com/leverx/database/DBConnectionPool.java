package com.leverx.database;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.sql.DriverManager.getConnection;
import static java.util.Collections.synchronizedList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class DBConnectionPool {

    public static final int FIRST = 0;
    public static final int ZERO = 0;
    private List<Connection> connectionInUse;
    private List<Connection> connectionOutOfUsage;

    private static final int MAX_POOL_CONNECTION_AMOUNT = 10;

    private static DataBaseProperties properties = new DataBaseProperties();
    private static final Logger logger = getLogger(DBConnectionPool.class);

    public DBConnectionPool() {
        connectionInUse = synchronizedList(new ArrayList<Connection>());
        connectionOutOfUsage = Stream
                .generate(DBConnectionPool::createConnection)
                .limit(MAX_POOL_CONNECTION_AMOUNT)
                .collect(toList());
        connectionOutOfUsage = synchronizedList(connectionOutOfUsage);
    }

    private static Connection createConnection() {
        try {
            Class.forName(properties.getDriverClassName());
            var url = properties.getDatabaseUrl();
            var user = properties.getDatabaseUsername();
            var password = properties.getDatabasePassword();
            var connection = getConnection(url, user, password);
            logger.info("Connection created");
            return connection;
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());

        } catch (ClassNotFoundException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    public Connection startSession() {
        while (connectionOutOfUsage.size() == ZERO) {
            logger.info("Wait for connection");
        }
        var connection = connectionOutOfUsage.get(FIRST);
        connectionInUse.add(connection);
        return connection;
    }

    public void finishSession(Connection connection) {
        connectionInUse.remove(connection);
        connectionOutOfUsage.add(connection);
    }
}
