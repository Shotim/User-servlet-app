package com.leverx.propertyloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.*;

import static com.leverx.constants.DataBaseCredentialsFields.DATABASE_PROPERTIES_FILE;

public class PropertyLoader {


    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLoader.class);
    private static List<String> propertyFiles = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    public PropertyLoader() {
        propertyFiles.add(DATABASE_PROPERTIES_FILE);
        propertyFiles.stream()
                .map(PropertyLoader::loadPropertiesFromFile)
                .forEach(properties::putAll);
    }

    private static Map<String, String> loadPropertiesFromFile(String fileName) {
        var properties = new Properties();
        try (var inputStream = PropertyLoader.class.getClassLoader().getResourceAsStream(DATABASE_PROPERTIES_FILE)) {
            properties.load(inputStream);
            LOGGER.debug("Property file with name {} exists", DATABASE_PROPERTIES_FILE);

            var propertiesMap = convertPropertiesToMap(properties);
            return propertiesMap;
        } catch (IOException e) {
            LOGGER.error("Property file with name {} was not found", DATABASE_PROPERTIES_FILE);
            throw new InternalServerErrorException(e);
        }
    }

    private static Map<String, String> convertPropertiesToMap(Properties properties) {
        Map<String, String> propertiesMap = properties
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        element -> (String) element.getKey(),
                        element -> (String) element.getValue()));
        LOGGER.debug("Properties were converted to Map");
        return propertiesMap;
    }


    public String getProperty(String key) {
        return properties.get(key);
    }
}