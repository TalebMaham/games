package com.gamesUP.gamesUP.controller;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.dto.WishlistRequest;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.services.WishlistService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;
    private final JwtUtil jwtUtil; 

    public WishlistController(WishlistService wishlistService, JwtUtil jwtUtil) {
        this.wishlistService = wishlistService;
        this.jwtUtil = jwtUtil ; 
    }

    @PostMapping
    public Wishlist create(@RequestBody WishlistRequest request) {
        return wishlistService.createWishlist(request.getUserId(), request.getGameIds());
    }
    

    @GetMapping
    public List<Wishlist> getWishlists(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String role = jwtUtil.getRoleFromToken(token);
        String email = jwtUtil.getUsernameFromToken(token);
    
        if ("ADMIN".equals(role)) {
            return wishlistService.getAll();
        } else {
            return wishlistService.getWishlistsByUserEmail(email);
        }
    }
    

    @GetMapping("/user/{userId}")
    public List<Wishlist> getByUser(@PathVariable Long userId) {
        return wishlistService.getByUserId(userId);
    }

}
