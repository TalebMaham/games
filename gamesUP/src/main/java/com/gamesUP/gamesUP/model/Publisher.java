package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    @JsonManagedReference("publisher-games")
    private List<Game> games;

    public Publisher() {}

    public Publisher(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Game> getGames() { return games; }

    public void setName(String name) { this.name = name; }
    public void setGames(List<Game> games) { this.games = games; }

    public void setId(Long id) {
        this.id = id;
    }
}
