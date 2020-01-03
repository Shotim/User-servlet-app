package com.leverx.core.loader.properties;

import com.leverx.core.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class PropertyLoader {

    private Map<String, String> propertiesMap;

    public PropertyLoader(String propertyFileName) {
        var properties = getPropertiesFromFile(propertyFileName);
        propertiesMap = getMapFromProperties(properties);
    }

    private Properties getPropertiesFromFile(String fileName) {
        try (InputStream inputStream = PropertyLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private Map<String, String> getMapFromProperties(Properties properties) {
        Map<String, String> propertiesMap = properties
                .entrySet()
                .stream()
                .collect(toMap(
                        element -> (String) element.getKey(),
                        element -> (String) element.getValue()));
        log.debug("Properties were converted to Map");
        return propertiesMap;
    }

    public Map<String, String> getPropertiesMap() {
        return this.propertiesMap;
    }

    public String getProperty(String propertyName) {
        return this.propertiesMap.get(propertyName);
    }
}
