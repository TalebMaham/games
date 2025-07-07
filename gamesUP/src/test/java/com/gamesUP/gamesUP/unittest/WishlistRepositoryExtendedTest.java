package com.gamesUP.gamesUP.unittest;

import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repositories.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WishlistRepositoryExtendedTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private WishlistRepository wishlistRepo;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private PublisherRepository publisherRepo;

    @Test
    void testWishlistWithFullEntities() {
        User user = new User("Alice", "alice@example.com",12, "secret", Role.CLIENT);
        user = userRepo.save(user);

        Author author = authorRepo.save(new Author("Shigeru Miyamoto"));
        Publisher publisher = publisherRepo.save(new Publisher("Nintendo"));

        Game game1 = new Game("Zelda");
        game1.setCategory(Category.FAMILLE);
        game1.setAuthor(author);
        game1.setPublisher(publisher);
        game1 = gameRepo.save(game1);

        Game game2 = new Game("Mario");
        game2.setCategory(Category.ENFANT);
        game2.setAuthor(author);
        game2.setPublisher(publisher);
        game2 = gameRepo.save(game2);

        Wishlist wishlist = new Wishlist(user, List.of(game1, game2));
        wishlist = wishlistRepo.save(wishlist);

        assertNotNull(wishlist.getId());
        assertEquals(2, wishlist.getGames().size());
        assertEquals("Alice", wishlist.getUser().getName());
        assertEquals(Category.FAMILLE, wishlist.getGames().get(0).getCategory());
        assertEquals("Nintendo", wishlist.getGames().get(0).getPublisher().getName());
        assertEquals("Shigeru Miyamoto", wishlist.getGames().get(0).getAuthor().getName());

        assertEquals(user.getId(), wishlist.getUser().getId());
    }
}

