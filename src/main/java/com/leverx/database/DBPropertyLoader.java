package com.leverx.database;

import com.leverx.propertyloader.PropertyLoader;

import static com.leverx.constants.DataBaseCredentialsFields.DATABASE_PROPERTIES_FILE;
import static com.leverx.constants.DataBaseCredentialsFields.DRIVER;
import static com.leverx.constants.DataBaseCredentialsFields.PASSWORD;
import static com.leverx.constants.DataBaseCredentialsFields.URL;
import static com.leverx.constants.DataBaseCredentialsFields.USERNAME;

public class DBPropertyLoader {

    private PropertyLoader loader;

    DBPropertyLoader() {
        loader = new PropertyLoader(DATABASE_PROPERTIES_FILE);
    }

    String getDriver() {
        return loader.getProperty(DRIVER);
    }

    String getUrl() {
        return loader.getProperty(URL);
    }

    String getUsername() {
        return loader.getProperty(USERNAME);
    }

    String getPassword() {
        return loader.getProperty(PASSWORD);
    }
}
