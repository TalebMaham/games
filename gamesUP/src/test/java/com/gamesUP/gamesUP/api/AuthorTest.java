package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.controller.AuthorController;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.services.AuthorService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthorController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class AuthorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAuthor() throws Exception {
        Author author = new Author("Jane Doe");
        author.setId(1L);

        when(authorService.create("Jane Doe")).thenReturn(author);

        mockMvc.perform(post("/api/authors")
                .param("name", "Jane Doe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        Author a1 = new Author("Alice");
        a1.setId(1L);
        Author a2 = new Author("Bob");
        a2.setId(2L);

        when(authorService.getAll()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Alice"))
            .andExpect(jsonPath("$[1].name").value("Bob"));
    }
}
