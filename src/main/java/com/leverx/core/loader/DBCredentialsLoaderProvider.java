package com.leverx.core.loader;

import static java.util.ServiceLoader.load;

public class DBCredentialsLoaderProvider {

    public static DBCredentialsLoader getDBCredentialsLoader() {
        var loader = load(DBCredentialsLoader.class);
        return loader.findFirst().orElseThrow();
    }
}