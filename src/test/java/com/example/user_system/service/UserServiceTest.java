package com.example.user_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.user_system.dto.request.UserRequestDto;
import com.example.user_system.dto.request.UserFilterDto;
import com.example.user_system.dto.request.UserSearchRequest;
import com.example.user_system.dto.response.UserResponseDto;
import com.example.user_system.entity.User;
import com.example.user_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequestDto defaultUserRequest;

    private UserFilterDto defaultUserFilter;

    private UserSearchRequest defaultUserSearchRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultUserRequest = new UserRequestDto();
        defaultUserRequest.setFirstName("Test");
        defaultUserRequest.setLastName("Test");
        defaultUserRequest.setContactInformation("userTest@example.com");

        defaultUserFilter = new UserFilterDto();
        defaultUserFilter.setFirstName("Test");

        defaultUserSearchRequest = new UserSearchRequest(defaultUserFilter, 0, 10);
    }

    @Test
    void createUser_shouldSaveUserAndReturnDto() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponseDto response = userService.createUser(defaultUserRequest);

        assertNotNull(response);
        assertEquals("Test", response.getFirstName());
        assertEquals("Test", response.getLastName());
        assertEquals("userTest@example.com", response.getContactInformation());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUsers_shouldReturnFilteredUsers() {
        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Test")
                .contactInformation("Test@example.com")
                .build();

        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(userPage);

        var response = userService.getUsers(defaultUserSearchRequest);

        assertNotNull(response);
        assertFalse(response.getUsers().isEmpty());
        assertEquals(1, response.getUsers().size());
        assertEquals("Test", response.getUsers().get(0).getFirstName());

        verify(userRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}