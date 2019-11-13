package com.leverx.driver;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class DataBaseProperties {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseProperties.class);
    @Getter
    private String driverClassName;
    @Getter
    private String databaseUrl;
    @Getter
    private String databaseUsername;
    @Getter
    private String databasePassword;

    DataBaseProperties() {
        setCredentials();
    }

    private void setCredentials() {
        Properties properties = new Properties();
        try (InputStream input = DataBaseProperties.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(input);
            driverClassName = properties.getProperty("driver");
            databaseUrl = properties.getProperty("url");
            databaseUsername = properties.getProperty("username");
            databasePassword = properties.getProperty("password");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
