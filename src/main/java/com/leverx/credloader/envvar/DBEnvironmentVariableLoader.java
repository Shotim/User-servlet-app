package com.leverx.credloader.envvar;

import com.leverx.credloader.DBCredentialsLoader;

import java.util.List;
import java.util.Map;

import static com.leverx.credloader.constants.DBEnvironmentVariableConstants.DB_PASSWORD_ENV;
import static com.leverx.credloader.constants.DBEnvironmentVariableConstants.DB_URL_ENV;
import static com.leverx.credloader.constants.DBEnvironmentVariableConstants.DB_USERNAME_ENV;

public class DBEnvironmentVariableLoader implements DBCredentialsLoader {

    private EnvironmentVariableLoader loader = new EnvironmentVariableLoader
            (List.of(DB_PASSWORD_ENV, DB_URL_ENV, DB_USERNAME_ENV));

    public String getUrl() {
        return loader.getEnvVariable(DB_URL_ENV);
    }

    public String getUsername() {
        return loader.getEnvVariable(DB_USERNAME_ENV);
    }

    public String getPassword() {
        return loader.getEnvVariable(DB_PASSWORD_ENV);
    }

    @Override
    public Map<String, String> getDBProperties() {
        return loader.getProperties();
    }
}
