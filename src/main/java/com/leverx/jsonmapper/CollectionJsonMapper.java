package com.leverx.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.additionalentities.CatsIdsList;

import javax.ws.rs.InternalServerErrorException;
import java.util.List;

public class CollectionJsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<Integer> convertFromJsonToIntegerList(String jsonIds) {
        try {
            CatsIdsList list = OBJECT_MAPPER.readValue(jsonIds, CatsIdsList.class);
            return list.getIds();
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

}
