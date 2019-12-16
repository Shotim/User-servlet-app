package com.leverx.credloader.props;

import com.leverx.credloader.DBCredentialsLoader;

import java.util.Map;

import static com.leverx.credloader.constants.DBPropertiesConstants.PROPERTY_FILE_NAME;

public class DBPropertiesLoader implements DBCredentialsLoader {

    private PropertyLoader loader;

    public DBPropertiesLoader() {
        loader = new PropertyLoader(PROPERTY_FILE_NAME);
    }

    @Override
    public Map<String, String> getDBProperties() {
        return loader.getPropertiesMap();
    }
}
