package com.gamesUP.gamesUP.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.enums.Category;
import com.gamesUP.gamesUP.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByCategory(Category category);
    List<Game> findByAuthorId(Long authorId);


}
