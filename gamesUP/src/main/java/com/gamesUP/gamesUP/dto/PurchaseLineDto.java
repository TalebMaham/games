package com.gamesUP.gamesUP.dto;


public class PurchaseLineDto {
    private Long gameId;
    private double prix;

    // Getters/setters

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
}
