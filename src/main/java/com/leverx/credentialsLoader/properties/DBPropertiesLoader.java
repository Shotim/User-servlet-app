package com.leverx.credentialsLoader.properties;

import com.leverx.credentialsLoader.DBCredentialsLoader;

import java.util.Map;

import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_PASSWORD;
import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_URL;
import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_USERNAME;

public class DBPropertiesLoader implements DBCredentialsLoader {

    public static final String PROPERTY_FILE_NAME = "hibernateDB.properties";
    private PropertyLoader loader;

    public DBPropertiesLoader() {
        loader = new PropertyLoader(PROPERTY_FILE_NAME);
    }

    @Override
    public Map<String, String> getDBPropertiesMap() {
        return loader.getPropertiesMap();
    }

    public String getUrl() {
        return loader.getProperty(DB_URL);
    }

    public String getUsername() {
        return loader.getProperty(DB_USERNAME);
    }

    public String getPassword() {
        return loader.getProperty(DB_PASSWORD);
    }
}
