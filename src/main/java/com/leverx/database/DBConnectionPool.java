package com.leverx.database;

import com.leverx.propertyloader.PropertyLoader;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

import static com.leverx.constants.DataBaseCredentialsFields.DRIVER;
import static com.leverx.constants.DataBaseCredentialsFields.PASSWORD;
import static com.leverx.constants.DataBaseCredentialsFields.URL;
import static com.leverx.constants.DataBaseCredentialsFields.USERNAME;
import static java.sql.DriverManager.getConnection;
import static org.slf4j.LoggerFactory.getLogger;

public class DBConnectionPool {

    private static final int MAX_POOL_CONNECTION_AMOUNT = 10;
    private static final Logger logger = getLogger(DBConnectionPool.class);
    private static final PropertyLoader properties = new PropertyLoader();
    private static DBConnectionPool connectionPool;
    private BlockingQueue<Connection> connectionOutOfUsage;

    private DBConnectionPool() {
        connectionOutOfUsage = new ArrayBlockingQueue<>(MAX_POOL_CONNECTION_AMOUNT);
        Stream.generate(DBConnectionPool::createConnection)
                .limit(MAX_POOL_CONNECTION_AMOUNT)
                .forEach(connectionOutOfUsage::add);
        logger.debug("DBConnectionPool instance was created");
    }

    public static DBConnectionPool getInstance() {
        if (connectionPool == null) {
            return new DBConnectionPool();
        }
        return connectionPool;
    }

    private static Connection createConnection() {
        try {
            Class.forName(properties.getProperty(DRIVER));
            var url = properties.getProperty(URL);
            var user = properties.getProperty(USERNAME);
            var password = properties.getProperty(PASSWORD);

            var connection = getConnection(url, user, password);
            logger.info("Connection created");
            return connection;

        } catch (SQLException e) {
            logger.error("Can't create connection to jdbc Driver. Credentials are wrong");
            throw new InternalServerErrorException(e);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find path to properties file");
            throw new InternalServerErrorException(e);
        }
    }

    public Connection takeConnection() {
        var connection = connectionOutOfUsage.remove();
        logger.debug("Connection was received from pool");
        return connection;
    }

    public void destroyConnection(Connection connection) {
        connectionOutOfUsage.add(connection);
        logger.debug("Connection was returned to pool");
    }
}
