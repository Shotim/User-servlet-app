package com.leverx.objectpool;

import com.leverx.database.DataBaseProperties;
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

public class ObjectPool {

    private List<Connection> connectionInUse;
    private List<Connection> connectionOutOfUsage;

    private static final int MAX_POOL_CONNECTION_AMOUNT = 10;

    private DataBaseProperties properties = new DataBaseProperties();
    private static final Logger logger = getLogger(ObjectPool.class);

    public ObjectPool() {
        connectionInUse = synchronizedList(new ArrayList<Connection>());
        connectionOutOfUsage = Stream
                .generate(this::createConnection)
                .limit(MAX_POOL_CONNECTION_AMOUNT)
                .collect(toList());
        connectionOutOfUsage = synchronizedList(connectionOutOfUsage);
    }

    private Connection createConnection() {
        try {
            var url = properties.getDatabaseUrl();
            var user = properties.getDatabaseUsername();
            var password = properties.getDatabasePassword();
            var connection = getConnection(url, user, password);
            logger.info("Connection created");
            return connection;
        } catch (SQLException e) {
            logger.error("SQL state: {}\n{}", e.getSQLState(), e.getMessage());
            return null;
        }
    }

    public Connection startSession() {
        var connection = connectionOutOfUsage.get(0);
        connectionInUse.add(connection);
        return connection;
    }

    public void endSession(Connection connection) {
        connectionInUse.remove(connection);
        connectionOutOfUsage.add(connection);
    }

}
