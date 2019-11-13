package com.leverx.user.driver;

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
    private String DRIVER_CLASS_NAME;
    @Getter
    private String DATABASE_URL;
    @Getter
    private String DATABASE_USERNAME;
    @Getter
    private String DATABASE_PASSWORD;

    DataBaseProperties() {
        setCredentials();
    }

    private void setCredentials() {
        Properties properties = new Properties();
        try (InputStream input = DataBaseProperties.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(input);
            DRIVER_CLASS_NAME = properties.getProperty("driver");
            DATABASE_URL = properties.getProperty("url");
            DATABASE_USERNAME = properties.getProperty("username");
            DATABASE_PASSWORD = properties.getProperty("password");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
