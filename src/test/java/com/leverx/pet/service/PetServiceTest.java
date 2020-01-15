package com.leverx.pet.service;

import com.leverx.cat.entity.Cat;
import com.leverx.dog.entity.Dog;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.repository.PetRepository;
import com.leverx.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class PetServiceTest {

    @Mock
    private PetRepository mockPetRepository;

    @InjectMocks
    private PetService dogService = getPetService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll_ShouldReturnPetOutputDtoCollection() {

        var id1 = 2;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var isCutEars1 = true;
        var name1 = "petya";

        var expectedPetOutputDto1 = new PetOutputDto(id1, name1, dateOfBirth1);
        expectedPetOutputDto1.setOwnerIds(asList(5, 1, 7));

        var id2 = 3;
        var dateOfBirth2 = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;
        var name2 = "petya";

        var expectedPetOutputDto2 = new PetOutputDto(id2, name2, dateOfBirth2);
        expectedPetOutputDto2.setOwnerIds(asList(5, 1));
        var expectedPetOutputDtoList = new ArrayList<>(List.of(expectedPetOutputDto1, expectedPetOutputDto2));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedPet1 = new Dog();
        expectedPet1.setId(id1);
        expectedPet1.setName(name1);
        expectedPet1.setDateOfBirth(dateOfBirth1);
        expectedPet1.setCutEars(isCutEars1);
        expectedPet1.setOwners(asList(userWithId5, userWithId1, userWithId7));

        var expectedPet2 = new Cat();
        expectedPet2.setId(id2);
        expectedPet2.setName(name2);
        expectedPet2.setDateOfBirth(dateOfBirth2);
        expectedPet2.setMiceCaughtNumber(miceCaughtNumber);
        expectedPet2.setOwners(asList(userWithId5, userWithId1));

        when(mockPetRepository.findAll()).thenReturn(List.of(expectedPet1, expectedPet2));

        //When
        var actualPetOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);
        verify(mockPetRepository, times(1)).findAll();
        verify(mockPetRepository).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList() {

        //Given
        when(mockPetRepository.findAll()).thenReturn(emptyList());
        var expectedPetOutputDtoList = new ArrayList<PetOutputDto>(emptyList());

        //When
        var actualPetOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);
        verify(mockPetRepository, times(1)).findAll();
        verify(mockPetRepository).findAll();
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingPetOutputDto() {

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
        verify(mockPetRepository, times(1)).findById(id);
        verify(mockPetRepository).findById(id);
    }

    @Test
    void findById_GivenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockPetRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> dogService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
        verify(mockPetRepository, times(1)).findById(anyInt());
        verify(mockPetRepository).findById(anyInt());
    }

    @Test
    void findByOwner_GivenExistingOwnerId_ShouldReturnExistingPetOutputDtoCollection() {

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
        verify(mockPetRepository, times(1)).findByOwner(ownerId);
        verify(mockPetRepository).findByOwner(ownerId);

    }

    @Test
    void findByOwner_GivenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockPetRepository.findByOwner(anyInt())).thenReturn(emptyList());
        var expectedPetOutputDtoList = new ArrayList<PetOutputDto>(emptyList());

        //When
        var actualPetOutputDtoList = dogService.findByOwner(anyInt());

        //Then
        assertEquals(expectedPetOutputDtoList, actualPetOutputDtoList);
        verify(mockPetRepository, times(1)).findByOwner(anyInt());
        verify(mockPetRepository).findByOwner(anyInt());
    }
}