package com.leverx.credloader.props;

import com.leverx.credloader.DBCredentialsLoader;

import java.util.Map;

import static com.leverx.credloader.constants.DBPropertiesConstants.DB_PASSWORD_PROP;
import static com.leverx.credloader.constants.DBPropertiesConstants.DB_URL_PROP;
import static com.leverx.credloader.constants.DBPropertiesConstants.DB_USERNAME_PROP;
import static com.leverx.credloader.constants.DBPropertiesConstants.PROPERTY_FILE_NAME;

public class DBPropertiesLoader implements DBCredentialsLoader {

    private PropertyLoader loader;

    public DBPropertiesLoader() {
        loader = new PropertyLoader(PROPERTY_FILE_NAME);
    }

    public String getUrl() {
        return loader.getProperty(DB_URL_PROP);
    }

    public String getUsername() {
        return loader.getProperty(DB_USERNAME_PROP);
    }

    public String getPassword() {
        return loader.getProperty(DB_PASSWORD_PROP);
    }

    @Override
    public Map<String, String> getDBProperties() {
        return loader.getPropertiesMap();
    }
}
