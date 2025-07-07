package com.gamesUP.gamesUP.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class PurchaseLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    private double prix;

    @ManyToOne(optional = false)
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private Purchase purchase;

    public PurchaseLine() {}

    public PurchaseLine(Game game, double prix, Purchase purchase) {
        this.game = game;
        this.prix = prix;
        this.purchase = purchase;
    }

    public Long getId() { return id; }
    public Game getGame() { return game; }
    public double getPrix() { return prix; }
    public Purchase getPurchase() { return purchase; }

    public void setGame(Game game) { this.game = game; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }
}
