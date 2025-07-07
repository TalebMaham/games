package com.gamesUP.gamesUP.unittest;

import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.repositories.UserRepository;
import com.gamesUP.gamesUP.repositories.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WishlistRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private WishlistRepository wishlistRepo;

    @Test
    void testWishlistPersistence() {
        User user = new User("Alice","alice@gmail.com", 12,   "alicepassword", Role.ADMIN);
        user = userRepo.save(user);

        Game game1 = gameRepo.save(new Game("Zelda"));
        Game game2 = gameRepo.save(new Game("Mario"));

        Wishlist wishlist = new Wishlist(user, List.of(game1, game2));
        wishlist = wishlistRepo.save(wishlist);

        assertEquals(1, wishlistRepo.count());
        assertEquals(2, wishlist.getGames().size());
        assertEquals("Alice", wishlist.getUser().getName());
    }
}
