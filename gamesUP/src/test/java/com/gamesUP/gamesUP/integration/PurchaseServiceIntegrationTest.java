package com.gamesUP.gamesUP.integration;

import com.gamesUP.gamesUP.dto.PurchaseLineDto;
import com.gamesUP.gamesUP.dto.PurchaseRequestDto;
import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repositories.*;
import com.gamesUP.gamesUP.services.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PurchaseServiceIntegrationTest {

    @Autowired private PurchaseService purchaseService;
    @Autowired private UserRepository userRepo;
    @Autowired private GameRepository gameRepo;
    @Autowired private AuthorRepository authorRepo;
    @Autowired private PublisherRepository publisherRepo;
    @Autowired private InventoryRepository inventoryRepo;

    @Test
    @Transactional
    void testCreatePurchaseAndDecrementStock() {
        // Création des entités nécessaires
        User user = userRepo.save(new User("TestUser", "test"+System.currentTimeMillis()+"@ex.com", 30, "pass", Role.CLIENT));
        Author author = authorRepo.save(new Author("Author1"));
        Publisher publisher = publisherRepo.save(new Publisher("Publisher1"));

        Game game = new Game("Test Game");
        game.setAuthor(author);
        game.setPublisher(publisher);
        game.setCategory(Category.FAMILLE);
        game = gameRepo.save(game);

        // Initialiser l'inventaire
        Inventory inventory = inventoryRepo.findAll().stream().findFirst().orElseGet(() -> inventoryRepo.save(new Inventory()));
        inventory.getStock().put(game, 3);
        inventoryRepo.save(inventory);

        // Création d’un achat avec ce jeu
        PurchaseLineDto lineDto = new PurchaseLineDto();
        lineDto.setGameId(game.getId());
        lineDto.setPrix(19.99);

        PurchaseRequestDto request = new PurchaseRequestDto();
        request.setUserId(user.getId());
        request.setLines(List.of(lineDto));

        Purchase purchase = purchaseService.createPurchase(request);

        assertNotNull(purchase.getId());
        assertEquals(1, purchase.getLines().size());

        // Vérifier que le stock a été décrémenté
        Inventory updated = inventoryRepo.findAll().get(0);
        Integer stockRestant = updated.getStock().get(game);
        assertEquals(2, stockRestant);
    }
}
