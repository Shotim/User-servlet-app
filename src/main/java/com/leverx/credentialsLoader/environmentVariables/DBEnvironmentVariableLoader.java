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

    public String getDBUrl() {
        return loader.getEnvVarValue(DB_URL);
    }

    public String getUsername() {
        return loader.getEnvVarValue(DB_USERNAME);
    }

    public String getPassword() {
        return loader.getEnvVarValue(DB_PASSWORD);
    }

}
