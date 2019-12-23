package com.leverx.credentialsLoader.envvar;

import com.leverx.credentialsLoader.DBCredentialsLoader;

import java.util.List;
import java.util.Map;

import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_PASSWORD_ENV;
import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_URL_ENV;
import static com.leverx.credentialsLoader.constants.DBEnvironmentVariableConstants.DB_USERNAME_ENV;

public class DBEnvironmentVariableLoader implements DBCredentialsLoader {

    private EnvironmentVariableLoader loader = new EnvironmentVariableLoader
            (List.of(DB_PASSWORD_ENV, DB_URL_ENV, DB_USERNAME_ENV));

    @Override
    public Map<String, String> getDBProperties() {
        return loader.getProperties();
    }
}
