package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
      List<Avis> findByGameId(Long gameId);
      List<Avis> findByNoteGreaterThanEqual(int note);
      List<Avis> findByGame(Game game);
}
