package com.leverx.pet.service;

import com.leverx.cat.entity.Cat;
import com.leverx.dog.entity.Dog;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.config.BeanFactory.getPetService;
import static com.leverx.core.utils.TestUtils.cutEars;
import static com.leverx.core.utils.TestUtils.id;
import static com.leverx.core.utils.TestUtils.validDateOfBirth;
import static com.leverx.core.utils.TestUtils.validMiceCaughtNumber;
import static com.leverx.core.utils.TestUtils.validName;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void findAll_GivenExpectedData_ShouldReturnPetOutputDtoCollection() {

        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var isCutEars1 = cutEars();
        var name1 = validName();
        var id2 = id();
        var dateOfBirth2 = validDateOfBirth();
        var miceCaughtNumber2 = validMiceCaughtNumber();
        var name2 = validName();

        var expectedPetOutputDto1 = new PetOutputDto(id1, name1, dateOfBirth1);

        var expectedPetOutputDto2 = new PetOutputDto(id2, name2, dateOfBirth2);

        var expectedPetOutputDtoList = newArrayList(expectedPetOutputDto1, expectedPetOutputDto2);

        var expectedPet1 = new Dog();
        expectedPet1.setId(id1);
        expectedPet1.setName(name1);
        expectedPet1.setDateOfBirth(dateOfBirth1);
        expectedPet1.setCutEars(isCutEars1);

        var expectedPet2 = new Cat();
        expectedPet2.setId(id2);
        expectedPet2.setName(name2);
        expectedPet2.setDateOfBirth(dateOfBirth2);
        expectedPet2.setMiceCaughtNumber(miceCaughtNumber2);

        when(mockPetRepository.findAll()).thenReturn(List.of(expectedPet1, expectedPet2));

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
        var id = id();
        var dateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var name = validName();

        var expectedPetOutputDto = new PetOutputDto(id, name, dateOfBirth);

        var expectedPet = new Dog();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setDateOfBirth(dateOfBirth);
        expectedPet.setCutEars(isCutEars);

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
        var id = id();
        var dateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var name = validName();

        var expectedPetOutputDto = new PetOutputDto(id, name, dateOfBirth);
        var expectedPetOutputDtoList = newArrayList(expectedPetOutputDto);

        var expectedPet = new Dog();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setDateOfBirth(dateOfBirth);
        expectedPet.setCutEars(isCutEars);

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

        //When
        var actualPetOutputDtoList = dogService.findByOwner(anyInt());

        //Then
        assertTrue(actualPetOutputDtoList.isEmpty());
        verify(mockPetRepository, times(1)).findByOwner(anyInt());
        verify(mockPetRepository).findByOwner(anyInt());
    }
}