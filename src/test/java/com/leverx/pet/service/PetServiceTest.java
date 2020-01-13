package com.leverx.pet.service;

import com.leverx.dog.entity.Dog;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.repository.PetRepository;
import com.leverx.pet.repository.PetRepositoryImpl;
import com.leverx.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.leverx.core.config.BeanFactory.getPetService;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class PetServiceTest {

    private PetRepository mockPetRepository = mock(PetRepositoryImpl.class);

    @InjectMocks
    private PetService dogService = getPetService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void shouldReturnPetList() {

        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var name = "petya";

        var expectedPetOutputDto = new PetOutputDto(id, name, dateOfBirth);
        expectedPetOutputDto.setOwnerIds(asList(5, 1, 7));
        var expectedPetOutputDtoList = new ArrayList<>(List.of(expectedPetOutputDto));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedPet = new Dog();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setDateOfBirth(dateOfBirth);
        expectedPet.setCutEars(isCutEars);
        expectedPet.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockPetRepository.findAll()).thenReturn(List.of(expectedPet));

        //When
        var actualPetOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);
    }

    @Test
    void shouldReturnEmptyList() {

        //Given
        when(mockPetRepository.findAll()).thenReturn(emptyList());
        var expectedPetOutputDtoList = new ArrayList<PetOutputDto>(emptyList());

        //When
        var actualPetOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);
    }

    @Test
    void givenExistingId_ShouldReturnExistingPet() {

        //Given
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var name = "petya";

        var expectedPetOutputDto = new PetOutputDto(id, name, dateOfBirth);
        expectedPetOutputDto.setOwnerIds(asList(5, 1, 7));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedPet = new Dog();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setDateOfBirth(dateOfBirth);
        expectedPet.setCutEars(isCutEars);
        expectedPet.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockPetRepository.findById(id)).thenReturn(Optional.of(expectedPet));

        //When
        var actualPet = dogService.findById(id);

        //Then
        assertEquals(expectedPetOutputDto, actualPet);
        verify(mockPetRepository).findById(id);
    }

    @Test
    void givenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockPetRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> dogService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
    }

    @Test
    void givenExistingOwnerId_ShouldReturnExistingPets() {

        //Given
        int ownerId = 1;
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var name = "petya";

        var expectedPetOutputDto = new PetOutputDto(id, name, dateOfBirth);
        expectedPetOutputDto.setOwnerIds(asList(5, 1, 7));
        var expectedPetOutputDtoList = new ArrayList<>(List.of(expectedPetOutputDto));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedPet = new Dog();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setDateOfBirth(dateOfBirth);
        expectedPet.setCutEars(isCutEars);
        expectedPet.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockPetRepository.findByOwner(ownerId)).thenReturn(List.of(expectedPet));

        //When
        var actualPetOutputDtoList = dogService.findByOwner(ownerId);

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);

    }

    @Test
    void givenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockPetRepository.findByOwner(anyInt())).thenReturn(emptyList());
        var expectedPetOutputDtoList = new ArrayList<PetOutputDto>(emptyList());

        //When
        var actualPetOutputDtoList = dogService.findByOwner(anyInt());

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);
    }
}