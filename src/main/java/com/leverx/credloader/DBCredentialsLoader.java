package com.leverx.credloader;

import java.util.Map;

public interface DBCredentialsLoader {

    Map<String, String> getDBProperties();
}
