package com.gamesUP.gamesUP.unittest;

import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.services.GameService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    private GameRepository gameRepo;
    private GameService gameService;

    @BeforeEach
    void setup() {
        gameRepo = mock(GameRepository.class);
        gameService = new GameService(gameRepo);
    }

    @Test
    void testGetAllGames() {
        when(gameRepo.findAll()).thenReturn(List.of(new Game("G1"), new Game("G2")));
        List<Game> games = gameService.getAllGames();
        assertEquals(2, games.size());
    }

    @Test
    void testAddGame() {
        Game game = new Game("New Game");
        when(gameRepo.save(game)).thenReturn(game);
        Game saved = gameService.addGame(game);
        assertEquals("New Game", saved.getTitle());
    }

    @Test
    void testGetGameByIdFound() {
        Game game = new Game("One");
        when(gameRepo.findById(1L)).thenReturn(Optional.of(game));
        Optional<Game> found = gameService.getGameById(1L);
        assertTrue(found.isPresent());
        assertEquals("One", found.get().getTitle());
    }

    @Test
    void testGetGameByIdNotFound() {
        when(gameRepo.findById(99L)).thenReturn(Optional.empty());
        assertTrue(gameService.getGameById(99L).isEmpty());
    }

    @Test
    void testGetGamesByCategory() {
        when(gameRepo.findByCategory(Category.ENFANT))
            .thenReturn(List.of(new Game("A"), new Game("B")));
        List<Game> list = gameService.getGamesByCategory(Category.ENFANT);
        assertEquals(2, list.size());
    }

    @Test
    void testGetGamesByAuthorId() {
        when(gameRepo.findByAuthorId(123L))
            .thenReturn(List.of(new Game("Zelda")));
        List<Game> list = gameService.getGamesByAuthorId(123L);
        assertEquals(1, list.size());
        assertEquals("Zelda", list.get(0).getTitle());
    }
}
