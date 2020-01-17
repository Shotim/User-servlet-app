package com.leverx.dog.servlet;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.core.utils.TestUtils.cutEars;
import static com.leverx.core.utils.TestUtils.id;
import static com.leverx.core.utils.TestUtils.invalidDateOfBirth;
import static com.leverx.core.utils.TestUtils.invalidName;
import static com.leverx.core.utils.TestUtils.validDateOfBirth;
import static com.leverx.core.utils.TestUtils.validName;
import static java.lang.String.join;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DogServletTest {

    @InjectMocks
    private DogServlet dogServlet = new DogServlet();

    @Mock
    private DogService mockDogService;

    @Mock
    private HttpServletRequest request;

    @Spy
    private HttpServletResponse response;

    @BeforeEach
    void init() {
        initMocks(this);
        response.setContentType("application/json; charset=UTF-8");
    }

    @Test
    void doGet_GivenUrlFindAllDogs_ShouldWriteThemToResponseBody() throws IOException {

        //Given
        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var isCutEars1 = cutEars();
        var name1 = validName();
        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var id2 = id();
        var dateOfBirth2 = validDateOfBirth();
        var isCutEars2 = cutEars();
        var name2 = validName();
        var expectedDogOutputDto2 = new DogOutputDto(id2, name2, dateOfBirth2);
        expectedDogOutputDto2.setCutEars(isCutEars2);

        var expectedDogOutputDtoList = newArrayList(expectedDogOutputDto1, expectedDogOutputDto2);

        var expectedResult = join("", fromEntityCollectionToJson(expectedDogOutputDtoList))
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs");

        when(mockDogService.findAll()).thenReturn(expectedDogOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        dogServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockDogService, times(1)).findAll();
        verify(mockDogService).findAll();

    }

    @Test
    void doGet_GivenUrlFindByIdDog_ShouldWriteItToResponseBody() throws IOException {

        //Given
        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var isCutEars1 = cutEars();
        var name1 = validName();

        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var expectedResult = join("", fromEntityToJson(expectedDogOutputDto1))
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs/" + id1);

        when(mockDogService.findById(id1)).thenReturn(expectedDogOutputDto1);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        dogServlet.doGet(request, response);

        //Then
        assertEquals(stringWriter.getBuffer().toString().trim(), expectedResult.trim());
        verify(mockDogService, times(1)).findById(id1);
        verify(mockDogService).findById(id1);
    }

    @Test
    void doGet_GivenUrlFindByIdDog_ShouldReturnEmptyBody() throws IOException {
        //Given
        var id = id();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs/"+id);

        when(mockDogService.findById(id)).thenThrow(ElementNotFoundException.class);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        dogServlet.doGet(request, response);

        //Then
        assertTrue(stringWriter.getBuffer().toString().isEmpty());
        verify(mockDogService, times(1)).findById(id);
        verify(mockDogService).findById(id);
    }

    @Test
    void doPost_GivenUrlSaveDogWithBody_ShouldSaveIt() throws IOException {

        //Given

        var id = id();
        var name = validName();
        var dateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var dogInputDto = new DogInputDto(name, dateOfBirth, isCutEars);

        var requestBody = fromEntityToJson(dogInputDto);

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);
        var dogOutputDto = new DogOutputDto(id, name, dateOfBirth);
        dogOutputDto.setCutEars(isCutEars);

        var stringBuffer = new StringBuffer("http://localhost:8080/dogs");

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockDogService.save(dogInputDto)).thenReturn(dogOutputDto);

        //When
        dogServlet.doPost(request, response);

        //Then
        verify(mockDogService).save(dogInputDto);
        verify(mockDogService, times(1)).save(dogInputDto);
    }

    @Test
    void doPost_GivenSaveUrlDogBody_ShouldThrownValidationFailedException() throws IOException {

        //Given

        var name = invalidName();
        var dateOfBirth = invalidDateOfBirth();
        var isCutEars = cutEars();

        var dogInputDto = new DogInputDto(name, dateOfBirth, isCutEars);

        var requestBody = fromEntityToJson(dogInputDto);

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs");

        var errorMessage = "Must be a date in the past or in the present; Name 'Dog' should be between 5 and 60 symbols";
        var expectedResult = "\"Must be a date in the past or in the present; Name 'Dog' should be between 5 and 60 symbols\"";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockDogService.save(dogInputDto)).thenThrow(new ValidationFailedException(errorMessage));
        when(response.getWriter()).thenReturn(printWriter);

        //When
        dogServlet.doPost(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();

        assertEquals(expectedResult, actualResult);
        verify(mockDogService).save(dogInputDto);
        verify(mockDogService, times(1)).save(dogInputDto);
    }
}