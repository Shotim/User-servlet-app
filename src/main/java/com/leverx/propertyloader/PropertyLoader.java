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
import java.util.stream.Collectors;

import static com.leverx.constants.DataBaseCredentialsFields.FILE_PATH;

public class PropertyLoader {


    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLoader.class);
    private static List<String> fileNames = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    public PropertyLoader() {
        fileNames.add(FILE_PATH);
        fileNames.stream()
                .map(PropertyLoader::loadPropertiesFromFile)
                .forEach(properties::putAll);
    }

    private static Map<String, String> loadPropertiesFromFile(String fileName) {
        var properties = new Properties();
        try (var inputStream = PropertyLoader.class.getClassLoader().getResourceAsStream(FILE_PATH)) {
            properties.load(inputStream);
            LOGGER.debug("Property file with name {} exists", FILE_PATH);
            Map<String, String> propertiesMap = properties
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            element -> (String) element.getKey(),
                            element -> (String) element.getValue()));
            LOGGER.debug("Properties were received");
            return propertiesMap;
        } catch (IOException e) {
            LOGGER.error("Property file with name {} was not found", FILE_PATH);
            throw new InternalServerErrorException(e);
        }
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}