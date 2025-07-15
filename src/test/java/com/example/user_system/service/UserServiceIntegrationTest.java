package com.example.user_system.service;

import com.example.user_system.TestContainersConfiguration;
import com.example.user_system.dto.request.UserFilterDto;
import com.example.user_system.dto.request.UserRequestDto;
import com.example.user_system.dto.request.UserSearchRequest;
import com.example.user_system.dto.response.PageResponse;
import com.example.user_system.dto.response.UserResponseDto;
import com.example.user_system.entity.User;
import com.example.user_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestContainersConfiguration.class)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserRequestDto defaultUserRequest;

    private UserFilterDto defaultUserFilter;

    private UserSearchRequest defaultUserSearchRequest;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        defaultUserRequest = new UserRequestDto();
        defaultUserRequest.setFirstName("Test");
        defaultUserRequest.setLastName("Test");
        defaultUserRequest.setContactInformation("userTest@example.com");

        defaultUserFilter = new UserFilterDto();
        defaultUserFilter.setFirstName("Test");

        defaultUserSearchRequest = new UserSearchRequest(defaultUserFilter, 0, 10);
    }

    private void saveDefaultUsers() {
        userRepository.save(User.builder()
                .firstName("Test")
                .lastName("Test")
                .contactInformation("Test@example.com")
                .build());
        userRepository.save(User.builder()
                .firstName("Test2")
                .lastName("Test2")
                .contactInformation("Test2@example.com")
                .build());
    }

    @Test
    void createUser_shouldPersistAndReturnDto() {
        UserResponseDto response = userService.createUser(defaultUserRequest);

        assertNotNull(response);
        assertEquals("Test", response.getFirstName());
        assertEquals("Test", response.getLastName());
        assertEquals("userTest@example.com", response.getContactInformation());

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("Test", users.get(0).getFirstName());
    }

    @Test
    void getUsers_shouldReturnPageWithUsers() {
        saveDefaultUsers();

        PageResponse<UserResponseDto> response = userService.getUsers(defaultUserSearchRequest);

        assertNotNull(response);
        assertFalse(response.getUsers().isEmpty());
        assertEquals(1, response.getUsers().size());
        assertEquals("Test", response.getUsers().get(0).getFirstName());
    }

}