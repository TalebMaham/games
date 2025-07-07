package com.gamesUP.gamesUP.services;



import com.gamesUP.gamesUP.dto.PurchaseLineDto;
import com.gamesUP.gamesUP.dto.PurchaseRequestDto;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.repositories.InventoryRepository;
import com.gamesUP.gamesUP.repositories.PurchaseRepository;
import com.gamesUP.gamesUP.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final UserRepository userRepo;
    private final GameRepository gameRepo;
    private final InventoryRepository inventoryRepo;

    public PurchaseService(PurchaseRepository purchaseRepo, UserRepository userRepo, GameRepository gameRepo, InventoryRepository inventoryRepo) {
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.inventoryRepo = inventoryRepo;
    }

    public Purchase createPurchase(PurchaseRequestDto dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        List<PurchaseLine> lines = new ArrayList<>();

        // Charger l'inventaire (supposé unique)
        Inventory inventory = inventoryRepo.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun inventaire trouvé"));

        // Vérifier le stock
        for (PurchaseLineDto lineDto : dto.getLines()) {
            Game game = gameRepo.findById(lineDto.getGameId()).orElseThrow();
            Integer stock = inventory.getStock().getOrDefault(game, 0);

            if (stock <= 0) {
                throw new RuntimeException("Jeu '" + game.getTitle() + "' non disponible");
            }

            // décrémenter temporairement (en mémoire)
            inventory.getStock().put(game, stock - 1);

            PurchaseLine line = new PurchaseLine(game, lineDto.getPrix(), null);
            lines.add(line);
        }

        // Créer l'achat
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setDate(new Date());
        purchase.setPaid(false);
        purchase.setDelivered(false);
        purchase.setArchived(false);

        // assigner les lignes à l'achat
        for (PurchaseLine line : lines) {
            line.setPurchase(purchase);
        }

        purchase.setLines(lines);

        // sauvegarder l'achat et l'inventaire mis à jour
        Purchase saved = purchaseRepo.save(purchase);
        inventoryRepo.save(inventory);

        return saved;
    }



    public List<Purchase> getAll() {
        return purchaseRepo.findAll();
    }

    public List<Purchase> getByUser(Long userId) {
        return purchaseRepo.findByUserId(userId);
    }

    public List<Purchase> getPurchasesByUserEmail(String email) {
        return purchaseRepo.findByUserEmail(email);
    }
}

