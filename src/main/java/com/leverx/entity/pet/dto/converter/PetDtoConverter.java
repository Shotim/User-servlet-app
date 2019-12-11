package com.leverx.entity.pet.dto.converter;

import com.leverx.entity.pet.dto.PetOutputDto;
import com.leverx.entity.pet.entity.Pet;
import com.leverx.entity.user.entity.User;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class PetDtoConverter {

    public static PetOutputDto petToPetOutputDto(Pet pet) {
        var id = pet.getId();
        var name = pet.getName();
        var dateOfBirth = pet.getDateOfBirth();
        var owners = pet.getOwners();
        var ownerIds = owners.stream()
                .map(User::getId)
                .collect(toList());
        return new PetOutputDto(id, name, dateOfBirth, ownerIds);
    }

    public static Collection<PetOutputDto> petCollectionToPetOutputDtoCollection(Collection<Pet> pets) {
        return pets.stream()
                .map(PetDtoConverter::petToPetOutputDto)
                .collect(toList());
    }
}
