package com.leverx.core.loader;

import java.util.Map;

public interface DBCredentialsLoader {

    Map<String, String> getDBPropertiesMap();

    String getUrl();

    String getPassword();

    String getUsername();
}
