package com.gamesUP.gamesUP.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    List<Wishlist> findByUser(User user);
}