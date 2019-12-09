package com.leverx.envvar.loader;

import static java.lang.System.getenv;

public class EnvironmentVariableLoader {

    public static String getEnvVarValue(String envVar) {
        return getenv(envVar);
    }

}
