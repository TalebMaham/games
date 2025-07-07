package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.controller.GameController;
import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repositories.UserRepository;
import com.gamesUP.gamesUP.services.GameService;
import com.gamesUP.gamesUP.services.RecommendationService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GameController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class GameTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private RecommendationService recommendationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllGames() throws Exception {
        Game game1 = new Game("Game One");
        Game game2 = new Game("Game Two");

        when(gameService.getAllGames()).thenReturn(List.of(game1, game2));

        mockMvc.perform(get("/api/games"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testAddGame() throws Exception {
        Game game = new Game("New Game");

        when(gameService.addGame(Mockito.any(Game.class))).thenReturn(game);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("New Game")));
    }

    @Test
    void testGetGameById() throws Exception {
        Game game = new Game("Unique Game");

        when(gameService.getGameById(1L)).thenReturn(Optional.of(game));

        mockMvc.perform(get("/api/games/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("Unique Game")));
    }

    @Test
    void testGetGamesByCategory() throws Exception {
        Game game = new Game("Game Cat");
        game.setCategory(Category.FAMILLE);

        when(gameService.getGamesByCategory(Category.FAMILLE)).thenReturn(List.of(game));

        mockMvc.perform(get("/api/games/category/FAMILLE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title", is("Game Cat")));
    }

    @Test
    void testGetGamesByAuthor() throws Exception {
        Game game = new Game("Author Game");

        when(gameService.getGamesByAuthorId(5L)).thenReturn(List.of(game));

        mockMvc.perform(get("/api/games/author/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title", is("Author Game")));
    }
}
