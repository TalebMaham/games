package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.controller.AvisController;
import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repositories.AvisRepository;

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

@WebMvcTest(controllers = AvisController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class AvisTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvisRepository avisRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAvis() throws Exception {
        Game game = new Game("Test Game");
        game.setId(1L);

        Avis avis = new Avis("Très bon jeu", 5, game);
        avis.setId(1L);

        when(avisRepository.save(Mockito.any(Avis.class))).thenReturn(avis);

        mockMvc.perform(post("/api/avis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avis)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.commentaire").value("Très bon jeu"))
            .andExpect(jsonPath("$.note").value(5));
    }

    @Test
    void testGetAvisByGame() throws Exception {
        Game game = new Game("Another Game");
        game.setId(2L);

        Avis avis = new Avis("Pas mal", 4, game);
        avis.setId(2L);

        when(avisRepository.findByGameId(2L)).thenReturn(List.of(avis));

        mockMvc.perform(get("/api/avis/game/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].commentaire").value("Pas mal"))
            .andExpect(jsonPath("$[0].note").value(4));
    }
}
