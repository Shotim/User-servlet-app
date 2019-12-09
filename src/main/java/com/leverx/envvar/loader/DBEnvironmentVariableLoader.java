package com.leverx.envvar.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.leverx.envvar.constants.DBEnvironmentVariableConstants.DB_PASSWORD_ENV;
import static com.leverx.envvar.constants.DBEnvironmentVariableConstants.DB_PASSWORD_PROP;
import static com.leverx.envvar.constants.DBEnvironmentVariableConstants.DB_URL_ENV;
import static com.leverx.envvar.constants.DBEnvironmentVariableConstants.DB_URL_PROP;
import static com.leverx.envvar.constants.DBEnvironmentVariableConstants.DB_USERNAME_ENV;
import static com.leverx.envvar.constants.DBEnvironmentVariableConstants.DB_USERNAME_PROP;
import static com.leverx.envvar.loader.EnvironmentVariableLoader.getEnvVarValue;

public class DBEnvironmentVariableLoader {

    public static Map<String, String> getDBProperties() {
        var propertyNames = List.of(DB_USERNAME_ENV, DB_PASSWORD_ENV, DB_URL_ENV);
        var properties = new HashMap<String, String>();
        propertyNames.forEach(property -> {
                    var envVarValue = getEnvVarValue(property);
                    String prop = "";
                    switch (property) {
                        case DB_URL_ENV:
                            prop = DB_URL_PROP;
                            break;
                        case DB_USERNAME_ENV:
                            prop = DB_USERNAME_PROP;
                            break;
                        case DB_PASSWORD_ENV:
                            prop = DB_PASSWORD_PROP;
                            break;
                    }
                    properties.put(prop, envVarValue);
                }
        );
        return properties;
    }
}
