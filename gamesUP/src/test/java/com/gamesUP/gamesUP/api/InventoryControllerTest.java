package com.gamesUP.gamesUP.api;

import com.gamesUP.gamesUP.controller.InventoryController;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.repositories.InventoryRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryRepository inventoryRepo;

    @MockBean
    private GameRepository gameRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetInventory() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Game1");

        Map<Game, Integer> stock = new HashMap<>();
        stock.put(game, 5);

        Inventory inventory = new Inventory();
        inventory.setStock(stock);

        when(inventoryRepo.findAll()).thenReturn(List.of(inventory));

        mockMvc.perform(get("/api/inventory"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.Game1").value(5));
    }

    @Test
    void testUpdateStock() throws Exception {
        Game game = new Game();
        game.setId(2L);
        game.setTitle("Game2");

        when(gameRepo.findById(2L)).thenReturn(Optional.of(game));

        Inventory inventory = new Inventory();
        when(inventoryRepo.findAll()).thenReturn(new ArrayList<>());

        when(inventoryRepo.save(Mockito.any(Inventory.class))).thenReturn(inventory);

        mockMvc.perform(post("/api/inventory/update")
                .param("gameId", "2")
                .param("quantity", "7")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Stock mis à jour pour Game2 : 7"));
    }

    @Test
    void testOutOfStockGames() throws Exception {
        Game game1 = new Game();
        game1.setTitle("GameA");

        Game game2 = new Game();
        game2.setTitle("GameB");

        Map<Game, Integer> stock = new HashMap<>();
        stock.put(game1, 0);
        stock.put(game2, 2);

        Inventory inventory = new Inventory();
        inventory.setStock(stock);

        when(inventoryRepo.findAll()).thenReturn(List.of(inventory));

        mockMvc.perform(get("/api/inventory/out-of-stock"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value("GameA"));
    }
}
