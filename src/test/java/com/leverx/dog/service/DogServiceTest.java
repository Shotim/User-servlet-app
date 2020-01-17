package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.entity.Dog;
import com.leverx.dog.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.config.BeanFactory.getDogService;
import static com.leverx.core.utils.TestUtils.cutEars;
import static com.leverx.core.utils.TestUtils.id;
import static com.leverx.core.utils.TestUtils.validDateOfBirth;
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

class DogServiceTest {

    @Mock
    private DogRepository mockDogRepository;

    @InjectMocks
    private DogService dogService = getDogService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll_GivenExpectedData_ShouldReturnDogOutputDtoCollection() {

        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var isCutEars1 = cutEars();
        var name1 = validName();
        var id2 = id();
        var dateOfBirth2 = validDateOfBirth();
        var isCutEars2 = cutEars();
        var name2 = validName();

        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var expectedDogOutputDto2 = new DogOutputDto(id2, name2, dateOfBirth2);
        expectedDogOutputDto2.setCutEars(isCutEars2);

        var expectedDogOutputDtoList = newArrayList(expectedDogOutputDto1, expectedDogOutputDto2);

        var expectedDog1 = new Dog();
        expectedDog1.setId(id1);
        expectedDog1.setName(name1);
        expectedDog1.setDateOfBirth(dateOfBirth1);
        expectedDog1.setCutEars(isCutEars1);

        var expectedDog2 = new Dog();
        expectedDog2.setId(id2);
        expectedDog2.setName(name2);
        expectedDog2.setDateOfBirth(dateOfBirth2);
        expectedDog2.setCutEars(isCutEars2);

        when(mockDogRepository.findAll()).thenReturn(List.of(expectedDog1, expectedDog2));

        //When
        var actualDogOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
        verify(mockDogRepository, times(1)).findAll();
        verify(mockDogRepository).findAll();
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingDogOutputDto() {

        //Given
        var id = id();
        var dateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var name = validName();

        var expectedDogOutputDto = new DogOutputDto(id, name, dateOfBirth);
        expectedDogOutputDto.setCutEars(isCutEars);

        var expectedDog = new Dog();
        expectedDog.setId(id);
        expectedDog.setName(name);
        expectedDog.setDateOfBirth(dateOfBirth);
        expectedDog.setCutEars(isCutEars);

        when(mockDogRepository.findById(id)).thenReturn(Optional.of(expectedDog));

        //When
        var actualDog = dogService.findById(id);

        //Then
        assertEquals(expectedDogOutputDto, actualDog);
        verify(mockDogRepository, times(1)).findById(id);
        verify(mockDogRepository).findById(id);
    }

    @Test
    void findById_GivenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockDogRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> dogService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
        verify(mockDogRepository, times(1)).findById(anyInt());
        verify(mockDogRepository).findById(anyInt());
    }

    @Test
    void findByOwner_GivenExistingOwnerId_ShouldReturnExistingDogOutputDtoCollection() {

        //Given
        int ownerId = id();
        var id = id();
        var dateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var name = validName();

        var expectedDogOutputDto = new DogOutputDto(id, name, dateOfBirth);
        expectedDogOutputDto.setCutEars(isCutEars);
        var expectedDogOutputDtoList = newArrayList(expectedDogOutputDto);

        var expectedDog = new Dog();
        expectedDog.setId(id);
        expectedDog.setName(name);
        expectedDog.setDateOfBirth(dateOfBirth);
        expectedDog.setCutEars(isCutEars);

        when(mockDogRepository.findByOwner(ownerId)).thenReturn(List.of(expectedDog));

        //When
        var actualDogOutputDtoList = dogService.findByOwner(ownerId);

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
        verify(mockDogRepository, times(1)).findByOwner(ownerId);
        verify(mockDogRepository).findByOwner(ownerId);
    }

    @Test
    void findByOwner_GivenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockDogRepository.findByOwner(anyInt())).thenReturn(emptyList());

        //When
        var actualDogOutputDtoList = dogService.findByOwner(anyInt());

        //Then
        assertTrue(actualDogOutputDtoList.isEmpty());
        verify(mockDogRepository, times(1)).findByOwner(anyInt());
        verify(mockDogRepository).findByOwner(anyInt());
    }

    @Test
    void save_GivenCorrectDogInputDto_ShouldSaveItAndReturnDogOutputDto() {

        //Given
        var dateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var name = validName();

        var dog = new Dog(name, dateOfBirth, isCutEars);

        var dogInputDto = new DogInputDto(name, dateOfBirth, isCutEars);

        var expectedDogOutputDto = new DogOutputDto(dog.getId(), name, dateOfBirth);
        expectedDogOutputDto.setCutEars(isCutEars);

        when(mockDogRepository.save(dog)).thenReturn(Optional.of(dog));

        //When
        var actualDogOutputDto = dogService.save(dogInputDto);

        //Then
        assertEquals(expectedDogOutputDto, actualDogOutputDto);
        verify(mockDogRepository, times(1)).save(dog);
        verify(mockDogRepository).save(dog);
    }
}