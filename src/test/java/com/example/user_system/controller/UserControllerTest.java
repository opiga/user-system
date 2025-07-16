package com.example.user_system.controller;

import com.example.user_system.dto.request.UserRequestDto;
import com.example.user_system.dto.response.PageResponse;
import com.example.user_system.dto.response.UserResponseDto;
import com.example.user_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private String userJson;

    private String searchJson;

    private String updateJson;

    @BeforeEach
    void setUp() {
        userJson = """
            {
              "firstName": "Test",
              "lastName": "Test",
              "contactInformation": "test@example.com",
              "email": "test@example.com"
            }
        """;

        searchJson = """
            {
              "filter": {
                "firstName": "Test",
                "lastName": "Test"
              },
              "page": 0,
              "size": 10
            }
        """;

        updateJson = """
            {
              "firstName": "Updated",
              "lastName": "User",
              "contactInformation": "123456789",
              "email": "updated@example.com"
            }
        """;
    }

    @Test
    void testCreateUser() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto("Test", "Test", "test@example.com");

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }

    @Test
    void testSearchUsers() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setFirstName("Test");
        userResponseDto.setLastName("Test");
        userResponseDto.setContactInformation("test@example.com");

        PageResponse<UserResponseDto> pageResponse = new PageResponse<>();
        pageResponse.setUsers(List.of(userResponseDto));
        pageResponse.setTotalElements(1L);
        pageResponse.setTotalPages(1);

        when(userService.getUsers(any())).thenReturn(pageResponse);

        mockMvc.perform(post("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].firstName").value("Test"))
                .andExpect(jsonPath("$.users[0].lastName").value("Test"));
    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;

        UserResponseDto updatedResponse = new UserResponseDto();
        updatedResponse.setFirstName("Updated");
        updatedResponse.setLastName("User");

        when(userService.updateUser(eq(userId), any(UserRequestDto.class))).thenReturn(updatedResponse);



        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

}