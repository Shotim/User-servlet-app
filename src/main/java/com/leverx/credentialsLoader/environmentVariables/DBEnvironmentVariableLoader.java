package com.leverx.credentialsLoader.environmentVariables;

import com.leverx.credentialsLoader.DBCredentialsLoader;

import java.util.List;
import java.util.Map;

import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_PASSWORD;
import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_URL;
import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_USERNAME;

public class DBEnvironmentVariableLoader implements DBCredentialsLoader {

    private EnvironmentVariableLoader loader = new EnvironmentVariableLoader
            (List.of(DB_PASSWORD, DB_URL, DB_USERNAME));

    @Override
    public Map<String, String> getDBPropertiesMap() {
        return loader.getPropertiesMap();
    }
}
