package com.gamesUP.gamesUP.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repositories.AvisRepository;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.repositories.PurchaseRepository;
import com.gamesUP.gamesUP.repositories.UserRepository;
import com.gamesUP.gamesUP.repositories.WishlistRepository;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class RecommendationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonApiUrl = "http://localhost:8000/recommendations/";

    @Autowired private UserRepository userRepository;
    @Autowired private PurchaseRepository purchaseRepository;
    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private AvisRepository avisRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private ObjectMapper objectMapper;

    public String getRecommendationsFromPython(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new RuntimeException("User not found");
        User user = optionalUser.get();

        List<Game> purchasedGames = purchaseRepository.findByUserId(user.getId()).stream()
            .flatMap(p -> p.getLines().stream().map(PurchaseLine::getGame)).distinct().toList();

        List<Game> wishlistGames = user.getWishlists().stream()
            .flatMap(w -> w.getGames().stream()).distinct().toList();

        Set<Long> wishlistGameIds = wishlistGames.stream()
            .map(Game::getId).collect(Collectors.toSet());

        List<Game> allGames = gameRepository.findAll();

        List<Map<String, Object>> purchases = new ArrayList<>();
        for (Game game : allGames) {
            boolean bought = purchasedGames.contains(game);
            boolean wished = wishlistGameIds.contains(game.getId());
            if (!bought && !wished) continue;

            double rating = 3.0;
            List<Avis> avisList = avisRepository.findByGame(game);
            if (!avisList.isEmpty()) {
                rating = avisList.stream().mapToInt(Avis::getNote).average().orElse(3.0);
            }

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("game_id", game.getId());
            entry.put("rating", rating);
            entry.put("author", game.getAuthor() != null ? game.getAuthor().getName() : "");
            entry.put("publisher", game.getPublisher() != null ? game.getPublisher().getName() : "");
            entry.put("category", game.getCategory() != null ? game.getCategory().name() : "");
            entry.put("from_wishlist", wished);
            purchases.add(entry);
        }

        List<Map<String, Object>> availableGames = allGames.stream().map(g -> {
            Map<String, Object> meta = new HashMap<>();
            meta.put("game_id", g.getId());
            meta.put("author", g.getAuthor() != null ? g.getAuthor().getName() : "");
            meta.put("publisher", g.getPublisher() != null ? g.getPublisher().getName() : "");
            meta.put("category", g.getCategory() != null ? g.getCategory().name() : "");
            return meta;
        }).collect(Collectors.toList());

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", user.getId());
        payload.put("age", user.getAge()); // ✅ nouvel attribut transmis
        payload.put("purchases", purchases);
        payload.put("available_games", availableGames);

        try {
            String json = objectMapper.writeValueAsString(payload);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi vers Python : " + e.getMessage(), e);
        }
    }
}

