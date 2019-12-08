package com.leverx.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.exception.InternalServerErrorException;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class EntityJsonConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String fromEntityToJson(T t) {
        try {
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public static <T> Collection<String> fromEntityCollectionToJson(Collection<T> objects) {
        return objects.stream()
                .map(EntityJsonConverter::fromEntityToJson)
                .collect(toList());
    }

    public static <T> T fromJsonToEntity(String object, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(object, tClass);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
