package com.leverx.credentialsLoader.properties;

import com.leverx.credentialsLoader.DBCredentialsLoader;

import java.util.Map;

public class DBPropertiesLoader implements DBCredentialsLoader {

    public static final String PROPERTY_FILE_NAME = "db.properties";
    private PropertyLoader loader;

    public DBPropertiesLoader() {
        loader = new PropertyLoader(PROPERTY_FILE_NAME);
    }

    @Override
    public Map<String, String> getDBProperties() {
        return loader.getPropertiesMap();
    }
}
