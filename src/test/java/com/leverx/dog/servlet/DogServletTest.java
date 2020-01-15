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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static javax.servlet.http.HttpServletResponse.SC_OK;
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
        var id1 = 2;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var isCutEars1 = true;
        var name1 = "petya";

        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var id2 = 3;
        var dateOfBirth2 = LocalDate.of(2020, 1, 1);
        var isCutEars2 = false;
        var name2 = "dog";

        var expectedDogOutputDto2 = new DogOutputDto(id2, name2, dateOfBirth2);
        expectedDogOutputDto2.setOwnerIds(emptyList());
        expectedDogOutputDto2.setCutEars(isCutEars2);

        var expectedDogOutputDtoList = new ArrayList<>(List.of(expectedDogOutputDto1, expectedDogOutputDto2));

        var expectedResult = "{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7],\"cutEars\":true}\n" +
                "{\"id\":3,\"name\":\"dog\",\"dateOfBirth\":\"2020-01-01\",\"ownerIds\":[],\"cutEars\":false}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs");

        when(mockDogService.findAll()).thenReturn(expectedDogOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);
//        when(response.getStatus()).thenCallRealMethod();
        //When
        dogServlet.doGet(request, response);
        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();
        assertEquals(SC_OK, response.getStatus());
        assertEquals(expectedResult, actualResult);
        verify(mockDogService, times(1)).findAll();
        verify(mockDogService).findAll();

    }

    @Test
    void doGet_GivenUrlFindByIdDog_ShouldWriteItToResponseBody() throws IOException {

        //Given
        var id1 = 2;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var isCutEars1 = true;
        var name1 = "petya";

        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var expectedResult = "{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7],\"cutEars\":true}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs/2");

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
        var id = 1;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs/1");

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
        var requestBody = "{\n" +
                "\t\"name\":\"NewDog\",\n" +
                "\t\"dateOfBirth\":\"2019-09-03\",\n" +
                "\t\"cutEars\": true\n" +
                "}";
        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var id = 3;
        var name = "NewDog";
        var dateOfBirth = LocalDate.of(2019, 9, 3);
        var isCutEars = true;

        var dogInputDto = new DogInputDto();
        dogInputDto.setName(name);
        dogInputDto.setDateOfBirth(dateOfBirth);
        dogInputDto.setCutEars(isCutEars);

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
        var requestBody = "{\n" +
                "\t\"name\":\"Dog\",\n" +
                "\t\"dateOfBirth\":\"2021-09-03\",\n" +
                "\t\"cutEars\": \"false\"\n" +
                "}";
        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var name = "Dog";
        var dateOfBirth = LocalDate.of(2021, 9, 3);
        var isCutEars = false;

        var dogInputDto = new DogInputDto();
        dogInputDto.setName(name);
        dogInputDto.setDateOfBirth(dateOfBirth);
        dogInputDto.setCutEars(isCutEars);

        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/dogs");

        var errorMessage = "\"Must be a date in the past or in the present; Name 'Dog' should be between 5 and 60 symbols\"";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockDogService.save(dogInputDto)).thenThrow(new ValidationFailedException(errorMessage));
        when(response.getWriter()).thenReturn(printWriter);
//        when(response.getStatus()).thenCallRealMethod();
        //When
        dogServlet.doPost(request, response);
        //Then
        verify(mockDogService).save(dogInputDto);
        verify(mockDogService, times(1)).save(dogInputDto);
    }
}