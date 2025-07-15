package com.example.user_system;

import com.example.user_system.dto.request.UserFilterDto;
import com.example.user_system.dto.request.UserRequestDto;
import com.example.user_system.dto.request.UserSearchRequest;
import com.example.user_system.dto.response.PageResponse;
import com.example.user_system.dto.response.UserResponseDto;
import com.example.user_system.entity.User;
import com.example.user_system.repository.UserRepository;
import com.example.user_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_shouldPersistAndReturnDto() {
        UserRequestDto request = new UserRequestDto();
        request.setFirstName("Test");
        request.setLastName("Test");
        request.setContactInformation("Test.doe@example.com");

        UserResponseDto response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("Test", response.getFirstName());
        assertEquals("Test", response.getLastName());
        assertEquals("Test.doe@example.com", response.getContactInformation());

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("Test", users.get(0).getFirstName());
    }

    @Test
    void getUsers_shouldReturnPageWithUsers() {
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

        UserFilterDto filter = new UserFilterDto();
        filter.setFirstName("Test");

        UserSearchRequest request = new UserSearchRequest(filter, 0, 10);

        PageResponse<UserResponseDto> response = userService.getUsers(request);

        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        assertEquals(1, response.getContent().size());
        assertEquals("Test", response.getContent().get(0).getFirstName());
    }

}