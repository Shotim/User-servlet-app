package com.leverx.pet.dto.converter;

import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.entity.Pet;
import com.leverx.user.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class PetDtoConverter {

    public static PetOutputDto petToPetOutputDto(Pet pet) {

        return petToPetOutputDto(pet, PetOutputDto.class);
    }

    public static Collection<PetOutputDto> petCollectionToPetOutputDtoCollection(Collection<Pet> pets) {
        return pets.stream()
                .map(PetDtoConverter::petToPetOutputDto)
                .collect(toList());
    }

    public static <T extends Pet, TOutputDto extends PetOutputDto> TOutputDto petToPetOutputDto(T pet, Class<TOutputDto> tOutputDtoClass) {
        var id = pet.getId();
        var name = pet.getName();
        var dateOfBirth = pet.getDateOfBirth();
        var owners = pet.getOwners();
        var ownerIds = owners.stream()
                .map(User::getId)
                .collect(toList());
        try {
            TOutputDto tOutputDto = tOutputDtoClass.getDeclaredConstructor(int.class, String.class, LocalDate.class)
                    .newInstance(id, name, dateOfBirth);
            tOutputDto.setOwnerIds(ownerIds);
            return tOutputDto;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
