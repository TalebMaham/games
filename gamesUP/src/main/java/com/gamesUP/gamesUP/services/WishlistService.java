package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repositories.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepo;
    private final UserRepository userRepo;
    private final GameRepository gameRepo;

    public WishlistService(WishlistRepository wishlistRepo, UserRepository userRepo, GameRepository gameRepo) {
        this.wishlistRepo = wishlistRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
    }

    public Wishlist createWishlist(Long userId, List<Long> gameIds) {
        User user = userRepo.findById(userId).orElseThrow();
        List<Game> games = gameRepo.findAllById(gameIds);
        Wishlist wishlist = new Wishlist(user, games);
        wishlistRepo.flush(); 
        return wishlistRepo.save(wishlist);
    }

    public List<Wishlist> getAll() {
        return wishlistRepo.findAll();
    }

    public List<Wishlist> getByUserId(Long userId) {
        return wishlistRepo.findByUserId(userId);
    }

    public List<Wishlist> getWishlistsByUserEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return wishlistRepo.findByUser(user);
    }
    
    
}
