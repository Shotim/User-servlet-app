package com.leverx.cat.mapper;

import com.google.gson.Gson;
import com.leverx.cat.entity.Cat;
import com.leverx.cat.entity.CatDto;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class CatJsonMapper {

    private static final Gson gson = new Gson();

    public static String convertFromCatToJson(Cat cat) {
        return gson.toJson(cat);
    }

    public static Collection<String> convertFromCatCollectionToJson(Collection<Cat> cats) {
        return cats.stream()
                .map(CatJsonMapper::convertFromCatToJson)
                .collect(toList());
    }

    public static CatDto convertFromJsonToCatDto(String cat) {
        return gson.fromJson(cat, CatDto.class);
    }
}
