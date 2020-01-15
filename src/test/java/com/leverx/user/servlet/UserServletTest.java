package com.leverx.user.servlet;

import com.leverx.cat.service.CatService;
import com.leverx.dog.service.DogService;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.service.PetService;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

class UserServletTest {

    @InjectMocks
    private UserServlet dogServlet = new UserServlet();

    @Mock
    private UserService userService;
    @Mock
    private CatService catService;
    @Mock
    private DogService dogService;
    @Mock
    private PetService petService;

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
    void doGet_GivenURlFindAllUsers_ShouldWriteThemToResponseBody() {
    }

    @Test
    void doGet_GivenURLFindAllUsers_ShouldReturnEmptyListToResponseBody() {
    }

    @Test
    void doGet_GivenURLFindById_ShouldWriteUserToResponseBody() {
    }

    @Test
    void doGet_GivenURLFindById_ShouldSetStatusCode404() {
    }

    @Test
    void doGet_GivenURlFindByNameUsers_ShouldWriteThemToResponseBody() {
    }

    @Test
    void doGet_GivenURLFindByNameUsers_ShouldReturnEmptyListToResponseBody() {
    }

    @Test
    void doGet_GivenURLFindUsersCats_ShouldWriteThemToResponseBody() {

    }

    @Test
    void doGet_GivenURLFindUsersCats_ShouldReturnEmptyList() {

    }

    @Test
    void doGet_GivenURLFindUsersDogs_ShouldWriteThemToResponseBody() {

    }

    @Test
    void doGet_GivenURLFindUsersDogs_ShouldReturnEmptyList() {

    }

    @Test
    void doGet_GivenURLFindUsersPets_ShouldWriteThemToResponseBody() {

    }

    @Test
    void doGet_GivenURLFindUsersPets_ShouldReturnEmptyList() {

    }

    @Test
    void doPost_GivenCorrectData_ShouldSaveItAndSetStatusCode201() {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Nowqwe\",\n" +
                "\t\"email\":\"qwert@yrty\",\n" +
                "\t\"animalPoints\": 78,\n" +
                "\t\"catsIds\":[1],\n" +
                "\t\"dogsIds\":[2]\n" +
                "}";

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var name = "Nowqwe";
        var email = "qwert@yrty";
        var animalPoints = 78;
        var catsIds = List.of(1);
        var dogsIds = List.of(2);

        var id = 3;
        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var petOutputDto1 = new PetOutputDto(1, "vasya", LocalDate.of(2019, 12, 1), List.of(1, 2, 3));
        var petOutputDto2 = new PetOutputDto(2, "petya", LocalDate.of(2019, 12, 1), List.of(1, 2, 3));

        var userOutputDto = new UserOutputDto(id, name, email, animalPoints, List.of(petOutputDto1, petOutputDto2));

        var stringBuffer = new StringBuffer("http://localhost:8080/users");

        //When
        //Then

    }

    @Test
    void doPost_GivenIncorrectData_ShouldWriteErrorMessages() {
    }

    @Test
    void doPost_GivenURLWithCorrectParametersForTransfer_ShouldPerformMoneyTransfer() {

    }

    @Test
    void doPost_GivenURLWithIncorrectParametersForTransfer_ShouldWriteErrorMessages() {

    }

    @Test
    void doDelete_GivenURLForDeleteUser_ShouldReturnStatusCode204() {
    }

    @Test
    void doPut_GivenCorrectData_ShouldUpdateUserAndReturnStatusCode200() {
    }

    @Test
    void doPut_GivenIncorrectData_ShouldReturnStatusCode422AndWriteErrorMessagesToResponseBody() {
    }
}