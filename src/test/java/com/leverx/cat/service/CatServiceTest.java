package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.core.exception.ElementNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.config.BeanFactory.getCatService;
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

class CatServiceTest {

    @Mock
    private CatRepository mockCatRepository;

    @InjectMocks
    private CatService catService = getCatService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll_GivenExpectedData_ShouldReturnCatOutputDtoCollection() {

        //Given
        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var miceCaughtNumber1 = validMiceCaughtNumber();
        var name1 = validName();
        var id2 = id();
        var dateOfBirth2 = validDateOfBirth();
        var miceCaughtNumber2 = validMiceCaughtNumber();
        var name2 = validName();

        var expectedCatOutputDto1 = new CatOutputDto(id1, name1, dateOfBirth1);
        expectedCatOutputDto1.setMiceCaughtNumber(miceCaughtNumber1);

        var expectedCatOutputDto2 = new CatOutputDto(id2, name2, dateOfBirth2);
        expectedCatOutputDto2.setMiceCaughtNumber(miceCaughtNumber2);

        var expectedCatOutputDtoList = newArrayList(expectedCatOutputDto1, expectedCatOutputDto2);

        var expectedCat1 = new Cat();
        expectedCat1.setId(id1);
        expectedCat1.setName(name1);
        expectedCat1.setDateOfBirth(dateOfBirth1);
        expectedCat1.setMiceCaughtNumber(miceCaughtNumber1);

        var expectedCat2 = new Cat();
        expectedCat2.setId(id2);
        expectedCat2.setName(name2);
        expectedCat2.setDateOfBirth(dateOfBirth2);
        expectedCat2.setMiceCaughtNumber(miceCaughtNumber2);
        when(mockCatRepository.findAll()).thenReturn(List.of(expectedCat1, expectedCat2));

        //When
        var actualCatOutputDtoList = catService.findAll();

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
        verify(mockCatRepository, times(1)).findAll();
        verify(mockCatRepository).findAll();
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingCatOutputDto() {

        //Given
        var id = id();
        var dateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();
        var name = validName();

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        var expectedCat = new Cat();
        expectedCat.setId(id);
        expectedCat.setName(name);
        expectedCat.setDateOfBirth(dateOfBirth);
        expectedCat.setMiceCaughtNumber(miceCaughtNumber);

        when(mockCatRepository.findById(id)).thenReturn(Optional.of(expectedCat));

        //When
        var actualCat = catService.findById(id);

        //Then
        assertEquals(expectedCatOutputDto, actualCat);
        verify(mockCatRepository, times(1)).findById(id);
        verify(mockCatRepository).findById(id);
    }

    @Test
    void findById_GivenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockCatRepository.findById(anyInt())).thenThrow(ElementNotFoundException.class);

        //When
        Executable whenStatement = () -> catService.findById(anyInt());

        //Then
        assertThrows(ElementNotFoundException.class, whenStatement);
        verify(mockCatRepository, times(1)).findById(anyInt());
        verify(mockCatRepository).findById(anyInt());
    }

    @Test
    void findByOwner_GivenExistingOwnerId_ShouldReturnExistingCatOutputDtoCollection() {

        //Given
        int ownerId = id();
        var id = id();
        var dateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();
        var name = validName();

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setMiceCaughtNumber(miceCaughtNumber);
        var expectedCatOutputDtoList = newArrayList(expectedCatOutputDto);

        var expectedCat = new Cat();
        expectedCat.setId(id);
        expectedCat.setName(name);
        expectedCat.setDateOfBirth(dateOfBirth);
        expectedCat.setMiceCaughtNumber(miceCaughtNumber);

        when(mockCatRepository.findByOwner(ownerId)).thenReturn(List.of(expectedCat));

        //When
        var actualCatOutputDtoList = catService.findByOwner(ownerId);

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
        verify(mockCatRepository, times(1)).findByOwner(ownerId);
        verify(mockCatRepository).findByOwner(ownerId);

    }

    @Test
    void findByOwner_GivenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockCatRepository.findByOwner(anyInt())).thenReturn(emptyList());

        //When
        var actualCatOutputDtoList = catService.findByOwner(anyInt());

        //Then
        assertTrue(actualCatOutputDtoList.isEmpty());
        verify(mockCatRepository, times(1)).findByOwner(anyInt());
        verify(mockCatRepository).findByOwner(anyInt());
    }

    @Test
    void save_GivenCorrectCatInputDto_ShouldSaveItAndReturnCatOutputDto() {

        //Given
        var dateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();
        var name = validName();

        var cat = new Cat(name, dateOfBirth, miceCaughtNumber);

        var catInputDto = new CatInputDto(name, dateOfBirth, miceCaughtNumber);

        var expectedCatOutputDto = new CatOutputDto(cat.getId(), name, dateOfBirth);
        expectedCatOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        when(mockCatRepository.save(cat)).thenReturn(Optional.of(cat));

        //When
        var actualCatOutputDto = catService.save(catInputDto);

        //Then
        assertEquals(expectedCatOutputDto, actualCatOutputDto);
        verify(mockCatRepository, times(1)).save(cat);
        verify(mockCatRepository).save(cat);
    }
}