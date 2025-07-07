package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.controller.PublisherController;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.services.PublisherService;

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

@WebMvcTest(controllers = PublisherController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PublisherTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePublisher() throws Exception {
        Publisher publisher = new Publisher("Ubisoft");
        publisher.setId(1L);

        when(publisherService.create("Ubisoft")).thenReturn(publisher);

        mockMvc.perform(post("/api/publishers")
                .param("name", "Ubisoft"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Ubisoft"));
    }

    @Test
    void testGetAllPublishers() throws Exception {
        Publisher p1 = new Publisher("EA");
        p1.setId(1L);
        Publisher p2 = new Publisher("Nintendo");
        p2.setId(2L);

        when(publisherService.getAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/publishers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("EA"))
            .andExpect(jsonPath("$[1].name").value("Nintendo"));
    }
}
