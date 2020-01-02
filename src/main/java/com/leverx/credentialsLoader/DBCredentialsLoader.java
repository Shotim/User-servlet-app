package com.leverx.credentialsLoader;

import java.util.Map;

public interface DBCredentialsLoader {

    Map<String, String> getDBPropertiesMap();

    String getUrl();

    String getPassword();

    String getUsername();
}
