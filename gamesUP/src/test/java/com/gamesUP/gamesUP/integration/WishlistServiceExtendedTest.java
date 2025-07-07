package com.gamesUP.gamesUP.integration;

import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repositories.*;
import com.gamesUP.gamesUP.services.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WishlistServiceExtendedTest {

    @Autowired private WishlistService wishlistService;
    @Autowired private UserRepository userRepo;
    @Autowired private GameRepository gameRepo;
    @Autowired private AuthorRepository authorRepo;
    @Autowired private PublisherRepository publisherRepo;

    @Test
    void testCreateWishlistWithRelatedEntities() {
        String uniqueEmail = "bob" + System.currentTimeMillis() + "@example.com";
        User user = userRepo.save(new User("Bob", uniqueEmail ,12,  "bobpassword", Role.CLIENT));

        Author author = authorRepo.save(new Author("AuthorX"));
        Publisher publisher = publisherRepo.save(new Publisher("PublisherY"));

        Game g1 = new Game("Zelda");
        g1.setAuthor(author);
        g1.setPublisher(publisher);
        g1.setCategory(Category.ENFANT);
        g1 = gameRepo.save(g1);

        Game g2 = new Game("Mario");
        g2.setAuthor(author);
        g2.setPublisher(publisher);
        g2.setCategory(Category.EXPERT);
        g2 = gameRepo.save(g2);

        Wishlist wishlist = wishlistService.createWishlist(user.getId(), List.of(g1.getId(), g2.getId()));

        assertNotNull(wishlist.getId());
        assertEquals(2, wishlist.getGames().size());
        assertEquals("Bob", wishlist.getUser().getName());

        for (Game game : wishlist.getGames()) {
            assertNotNull(game.getAuthor());
            assertNotNull(game.getPublisher());
            assertNotNull(game.getCategory());
            assertEquals("AuthorX", game.getAuthor().getName());
            assertEquals("PublisherY", game.getPublisher().getName());
        }
    }
}
