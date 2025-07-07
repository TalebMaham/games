package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.repositories.InventoryRepository;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepo;
    private final GameRepository gameRepo;

    public InventoryController(InventoryRepository inventoryRepo, GameRepository gameRepo) {
        this.inventoryRepo = inventoryRepo;
        this.gameRepo = gameRepo;
    }


    @GetMapping
    public Map<String, Integer> getInventory() {
        Inventory inventory = inventoryRepo.findAll().stream().findFirst()
                .orElseGet(() -> inventoryRepo.save(new Inventory()));

        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<Game, Integer> entry : inventory.getStock().entrySet()) {
            result.put(entry.getKey().getTitle(), entry.getValue());
        }
        return result;
    }

    @PostMapping("/update")
    public String updateStock(@RequestParam Long gameId, @RequestParam int quantity) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Jeu introuvable"));

        Inventory inventory = inventoryRepo.findAll().stream().findFirst()
                .orElseGet(() -> inventoryRepo.save(new Inventory()));

        inventory.getStock().put(game, quantity);
        inventoryRepo.save(inventory);

        return "Stock mis à jour pour " + game.getTitle() + " : " + quantity;
    }

    @GetMapping("/out-of-stock")
    public List<String> getOutOfStockGames() {
        Inventory inventory = inventoryRepo.findAll().stream().findFirst()
                .orElse(new Inventory());

        List<String> outOfStock = new ArrayList<>();
        for (Map.Entry<Game, Integer> entry : inventory.getStock().entrySet()) {
            if (entry.getValue() <= 0) {
                outOfStock.add(entry.getKey().getTitle());
            }
        }
        return outOfStock;
    }
}
