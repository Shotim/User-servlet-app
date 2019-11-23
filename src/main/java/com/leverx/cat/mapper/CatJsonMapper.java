package com.leverx.cat.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatDto;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class CatJsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String convertFromCatToJson(Cat cat) {
        try {
            return OBJECT_MAPPER.writeValueAsString(cat);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public static Collection<String> convertFromCatCollectionToJson(Collection<Cat> cats) {
        return cats.stream()
                .map(CatJsonMapper::convertFromCatToJson)
                .collect(toList());
    }

    public static CatDto convertFromJsonToCatDto(String cat) {
        try {
            return OBJECT_MAPPER.readValue(cat, CatDto.class);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}