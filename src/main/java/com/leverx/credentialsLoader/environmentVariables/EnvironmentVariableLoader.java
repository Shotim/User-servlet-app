package com.leverx.credentialsLoader.environmentVariables;

import java.util.Collection;
import java.util.Map;
import java.util.stream.*;

import static java.lang.System.getenv;
import static java.util.function.Function.identity;

public class EnvironmentVariableLoader {

    private Map<String, String> propertiesMap;

    public EnvironmentVariableLoader(Collection<String> envVariables) {
        this.propertiesMap = getEnvVariables(envVariables);
    }

    public Map<String, String> getEnvVariables(Collection<String> envVariables) {
        return envVariables.stream()
                .collect(Collectors.toMap(identity(), this::getEnvVarValue));
    }

    public String getEnvVarValue(String envVar) {
        return getenv(envVar);
    }

    public Map<String, String> getPropertiesMap() {
        return this.propertiesMap;
    }
}
