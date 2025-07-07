package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repositories.UserRepository;
import com.gamesUP.gamesUP.services.GameService;
import com.gamesUP.gamesUP.services.RecommendationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final RecommendationService recommendationService; 
    private final JwtUtil jwtUtil; 
    private final UserRepository userRepository;
    
    public GameController(GameService gameService, RecommendationService recommendationService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.gameService = gameService;
        this.recommendationService = recommendationService ; 
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository ; 
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game) {
        return ResponseEntity.ok(gameService.addGame(game));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<Game>> getByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(gameService.getGamesByCategory(category));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Game>> getByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(gameService.getGamesByAuthorId(authorId));
    }


    @GetMapping("/recommend")
    public ResponseEntity<?> getRecommendations(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getUsernameFromToken(token);

            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }

            Long userId = optionalUser.get().getId();
            String response = recommendationService.getRecommendationsFromPython(email);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur recommandation : " + e.getMessage());
        }
}

}
