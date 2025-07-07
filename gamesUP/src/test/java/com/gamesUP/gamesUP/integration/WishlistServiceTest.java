package com.gamesUP.gamesUP.integration;

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
public class WishlistServiceTest {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Test
    void testCreateWishlist() {
        User user = userRepo.save(new User("Bob", "bob@example.com",12,  "bobpassword", Role.ADMIN)
        );
        Game g1 = gameRepo.save(new Game("Tetris"));
        Game g2 = gameRepo.save(new Game("Pong"));

        Wishlist wishlist = wishlistService.createWishlist(user.getId(), List.of(g1.getId(), g2.getId()));

        assertNotNull(wishlist.getId());
        assertEquals(2, wishlist.getGames().size());
        assertEquals("Bob", wishlist.getUser().getName());
    }
}