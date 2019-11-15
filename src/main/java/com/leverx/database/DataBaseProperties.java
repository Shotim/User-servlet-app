package com.leverx.database;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static lombok.AccessLevel.PRIVATE;
import static org.slf4j.LoggerFactory.getLogger;

@FieldDefaults(level = PRIVATE)
public class DataBaseProperties {

    private static final String PROPERTY_FILE = "database.properties";
    private static final Logger logger = getLogger(DataBaseProperties.class);
    @Getter
    private String driverClassName;
    @Getter
    private String databaseUrl;
    @Getter
    private String databaseUsername;
    @Getter
    private String databasePassword;

    public DataBaseProperties() {
        var properties = new Properties();
        try (InputStream input = DataBaseProperties.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            properties.load(input);
            logger.debug("Property file with name {} exists", PROPERTY_FILE);
            driverClassName = properties.getProperty("db.driver");
            databaseUrl = properties.getProperty("db.url");
            databaseUsername = properties.getProperty("db.username");
            databasePassword = properties.getProperty("db.password");
            logger.debug("Credentials were received");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
