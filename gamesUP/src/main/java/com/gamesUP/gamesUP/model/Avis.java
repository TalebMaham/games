package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;

@Entity
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentaire;

    private int note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    public Avis() {}

    public Avis(String commentaire, int note, Game game) {
        this.commentaire = commentaire;
        this.note = note;
        this.game = game;
    }

    public Long getId() { return id; }
    public String getCommentaire() { return commentaire; }
    public int getNote() { return note; }
    public Game getGame() { return game; }

    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public void setNote(int note) { this.note = note; }
    public void setGame(Game game) { this.game = game; }

    public void setId(Long id) {
        this.id = id;
    }
}
