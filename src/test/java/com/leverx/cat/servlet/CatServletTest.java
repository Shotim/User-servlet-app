package com.leverx.cat.servlet;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.service.CatService;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
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
import static com.leverx.core.utils.TestUtils.id;
import static com.leverx.core.utils.TestUtils.invalidDateOfBirth;
import static com.leverx.core.utils.TestUtils.invalidName;
import static com.leverx.core.utils.TestUtils.randomInvalidMiceCaughtNumber;
import static com.leverx.core.utils.TestUtils.validDateOfBirth;
import static com.leverx.core.utils.TestUtils.validMiceCaughtNumber;
import static com.leverx.core.utils.TestUtils.validName;
import static java.lang.String.join;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CatServletTest {

    @InjectMocks
    private CatServlet catServlet = new CatServlet();

    @Mock
    private CatService mockCatService;

    @Mock
    private HttpServletRequest request;

    @Spy
    private HttpServletResponse response;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void doGet_GivenUrlFindAllCats_ShouldWriteThemToResponseBody() throws IOException {

        //Given
        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var miceCaughtNumber1 = validMiceCaughtNumber();
        var name1 = validName();

        var expectedCatOutputDto1 = new CatOutputDto(id1, name1, dateOfBirth1);
        expectedCatOutputDto1.setMiceCaughtNumber(miceCaughtNumber1);

        var id2 = id();
        var dateOfBirth2 = validDateOfBirth();
        var miceCaughtNumber2 = validMiceCaughtNumber();
        var name2 = validName();

        var expectedCatOutputDto2 = new CatOutputDto(id2, name2, dateOfBirth2);
        expectedCatOutputDto2.setOwnerIds(emptyList());
        expectedCatOutputDto2.setMiceCaughtNumber(miceCaughtNumber2);
        var expectedCatOutputDtoList = newArrayList(expectedCatOutputDto1, expectedCatOutputDto2);

        var expectedResult = join("", fromEntityCollectionToJson(expectedCatOutputDtoList))
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats");

        when(mockCatService.findAll()).thenReturn(expectedCatOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockCatService, times(1)).findAll();
        verify(mockCatService).findAll();
    }

    @Test
    void doGet_GivenUrlFindByIdCat_ShouldWriteItToResponseBody() throws IOException {

        //Given
        var id1 = id();
        var dateOfBirth1 = validDateOfBirth();
        var miceCaughtNumber1 = validMiceCaughtNumber();
        var name1 = validName();

        var expectedCatOutputDto1 = new CatOutputDto(id1, name1, dateOfBirth1);
        expectedCatOutputDto1.setMiceCaughtNumber(miceCaughtNumber1);

        var expectedResult = join("", fromEntityToJson(expectedCatOutputDto1))
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats/" + id1);

        when(mockCatService.findById(id1)).thenReturn(expectedCatOutputDto1);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();

        assertEquals(expectedResult, actualResult);
        verify(mockCatService, times(1)).findById(id1);
        verify(mockCatService).findById(id1);
    }

    @Test
    void doGet_GivenUrlFindByIdCat_ShouldReturnEmptyBody() throws IOException {

        //Given
        var id = id();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats/" + id);

        when(mockCatService.findById(id)).thenThrow(ElementNotFoundException.class);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doGet(request, response);

        //Then
        assertTrue(stringWriter.getBuffer().toString().isEmpty());
        verify(mockCatService, times(1)).findById(id);
        verify(mockCatService).findById(id);
    }

    @Test
    void doPost_GivenSaveUrlWithCorrectCatBody_ShouldSaveIt() throws IOException {

        //Given

        var id = id();
        var name = validName();
        var dateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();

        var catInputDto = new CatInputDto(name, dateOfBirth, miceCaughtNumber);

        var catOutputDto = new CatOutputDto(id, name, dateOfBirth);
        catOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats");
        var requestBody = fromEntityToJson(catInputDto);

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockCatService.save(catInputDto)).thenReturn(catOutputDto);

        //When
        catServlet.doPost(request, response);

        //Then
        verify(mockCatService).save(catInputDto);
        verify(mockCatService, times(1)).save(catInputDto);
    }

    @Test
    void doPost_GivenSaveUrlCatBody_ShouldThrownValidationFailedException() throws IOException {

        //Given

        var name = invalidName();
        var dateOfBirth = invalidDateOfBirth();
        var miceCaughtNumber = randomInvalidMiceCaughtNumber();

        var catInputDto = new CatInputDto(name, dateOfBirth, miceCaughtNumber);

        var requestBody = fromEntityToJson(catInputDto);

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats");

        var errorMessage = "Must be a date in the past or in the present;" +
                " Number '-13' must be positive or zero; Name 'Cat' should be between 5 and 60 symbols";

        var expectedResult = "\"Must be a date in the past or in the present;" +
                " Number '-13' must be positive or zero; Name 'Cat' should be between 5 and 60 symbols\"";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockCatService.save(catInputDto)).thenThrow(new ValidationFailedException(errorMessage));
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doPost(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();

        assertEquals(expectedResult, actualResult);
        verify(mockCatService).save(catInputDto);
        verify(mockCatService, times(1)).save(catInputDto);
    }
}