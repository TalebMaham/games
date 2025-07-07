package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repositories.GameRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }
    

    public List<Game> getGamesByCategory(Category category) {
        return gameRepository.findByCategory(category);
    }

    public List<Game> getGamesByAuthorId(Long authorId) {
        return gameRepository.findByAuthorId(authorId);
    }
    
}
