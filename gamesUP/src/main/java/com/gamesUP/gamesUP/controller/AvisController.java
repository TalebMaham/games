package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.repositories.AvisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avis")
public class AvisController {

    @Autowired
    private AvisRepository avisRepository;

    @PostMapping
    public Avis createAvis(@RequestBody Avis avis) {
        return avisRepository.save(avis);
    }

    @GetMapping("/game/{gameId}")
    public List<Avis> getAvisByGame(@PathVariable Long gameId) {
        return avisRepository.findByGameId(gameId);
    }
}
