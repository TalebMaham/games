package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.dto.PurchaseRequestDto;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.services.PurchaseService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService service;
    private final JwtUtil jwtUtil; 

    public PurchaseController(PurchaseService service,JwtUtil jwtUtil ) {
        this.service = service;
        this.jwtUtil = jwtUtil ; 
    }

    @PostMapping
    public Purchase create(@RequestBody PurchaseRequestDto dto) {
        return service.createPurchase(dto);
    }

    @GetMapping
    public List<Purchase> getPurchases(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String role = jwtUtil.getRoleFromToken(token);
        String email = jwtUtil.getUsernameFromToken(token);

        if ("ADMIN".equals(role)) {
            return service.getAll();
        } else {
            return service.getPurchasesByUserEmail(email);
        }
    }

    @GetMapping("/user/{userId}")
    public List<Purchase> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }
}
