package com.leverx.entity.pet.dto.converter;

import com.leverx.entity.pet.dto.PetOutputDto;
import com.leverx.entity.pet.entity.Pet;

import java.util.Collection;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class PetDtoConverter {

    public static PetOutputDto petToPetOutputDto(Pet pet) {
        var id = pet.getId();
        var name = pet.getName();
        var dateOfBirth = pet.getDateOfBirth();
        var owner = pet.getOwner();
        if (nonNull(owner)) {
            var ownerId = owner.getId();
            return new PetOutputDto(id, name, dateOfBirth, ownerId);
        }
        return new PetOutputDto(id, name, dateOfBirth, null);
    }

    public static Collection<PetOutputDto> petCollectionToPetOutputDtoCollection(Collection<Pet> pets) {
        return pets.stream()
                .map(PetDtoConverter::petToPetOutputDto)
                .collect(toList());
    }
}
