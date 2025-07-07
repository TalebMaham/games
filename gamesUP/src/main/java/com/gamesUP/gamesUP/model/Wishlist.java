package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"wishlists", "password"}) 
    @ManyToOne(optional = false)
    private User user;

    @ManyToMany
    private List<Game> games;

    public Wishlist() {}
    public Wishlist(User user, List<Game> games) {
        this.user = user;
        this.games = games;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<Game> getGames() { return games; }

    public void setUser(User user) { this.user = user; }
    public void setGames(List<Game> games) { this.games = games; }

    public void setId(Long id) {
        this.id = id;
    }
}
