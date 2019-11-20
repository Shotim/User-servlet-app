package com.leverx.propertyloader;

import com.leverx.database.DBPropertyLoader;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

public class PropertyLoader {

    private static final Logger LOGGER = getLogger(PropertyLoader.class);
    private Map<String, String> propertiesMap;

    public PropertyLoader(String fileName) {
        var properties = getPropertiesFromFile(fileName);
        propertiesMap = getMapFromProperties(properties);
    }

    private Properties getPropertiesFromFile(String fileName) {
        try (InputStream inputStream = DBPropertyLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    private Map<String, String> getMapFromProperties(Properties properties) {
        Map<String, String> propertiesMap = properties
                .entrySet()
                .stream()
                .collect(toMap(
                        element -> (String) element.getKey(),
                        element -> (String) element.getValue()));
        LOGGER.debug("Properties were converted to Map");
        return propertiesMap;
    }

    public String getProperty(String propertyKey) {
        return propertiesMap.get(propertyKey);
    }
}
