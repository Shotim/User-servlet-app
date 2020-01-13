package com.leverx.user.service;

import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static com.leverx.core.config.BeanFactory.getUserService;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private UserRepository mockUserRepository = mock(UserRepositoryImpl.class);

    @InjectMocks
    private UserService userService = getUserService();

    @Test
    void shouldReturnUserList() {
    }

    @Test
    void shouldReturnEmptyList() {
    }

    @Test
    void givenExistingId_ShouldReturnExistingUser() {
    }

    @Test
    void givenNonexistentId_ShouldThrownElementNotFoundException() {
    }

    @Test
    void givenExistingName_ShouldReturnExistingPets() {
    }

    @Test
    void givenExistingName_ShouldReturnEmptyList() {
    }

    @Test
    void givenUserInputDto_ShouldSaveItAndReturnUserOutputDto() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateById() {
    }

    @Test
    void pointsTransfer() {
    }
}