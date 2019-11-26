package com.leverx.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class EntityJsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();



    public static <T> String convertFromEntityToJson(T t) {
        try {
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public static <T> Collection<String> convertFromEntityCollectionToJson(Collection<T> objects) {
        return objects.stream()
                .map(EntityJsonMapper::convertFromEntityToJson)
                .collect(toList());
    }

    public static <T> T convertFromJsonToEntity(String object, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(object, tClass);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
