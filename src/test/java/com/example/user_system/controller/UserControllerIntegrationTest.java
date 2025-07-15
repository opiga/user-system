package com.example.user_system.controller;

import com.example.user_system.TestContainersConfiguration;
import com.example.user_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String userJson;

    private String searchJson;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userJson = """
            {
              "firstName": "user",
              "lastName": "userLastName",
              "contactInformation": "info",
              "email": "user@gmail.com"
            }
        """;

        searchJson = """
            {
              "filter": {
                "firstName": "user",
                "lastName": "userLastName"
              },
              "page": 0,
              "size": 10
            }
        """;
    }

    private void createUser() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
    }

    @Test
    void testCreateUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("user"))
                .andExpect(jsonPath("$.lastName").value("userLastName"));
    }

    @Test
    void testSearchUsers() throws Exception {
        createUser();
        mockMvc.perform(post("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].firstName").value("user"))
                .andExpect(jsonPath("$.users[0].lastName").value("userLastName"));
    }
}