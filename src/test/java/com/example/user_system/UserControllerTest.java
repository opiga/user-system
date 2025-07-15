package com.example.user_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String userJson;

    private String searchJson;

    @BeforeEach
    void setUp() {
        userJson = """
            {
              "firstName": "Test",
              "lastName": "Test",
              "contactInformation": "test@example.com"
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
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }

    @Test
    void testSearchUsers() throws Exception {
        createUser();

        mockMvc.perform(post("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Test"))
                .andExpect(jsonPath("$.content[0].lastName").value("Test"));
    }
}