package com.gamesUP.gamesUP.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gamesUP.gamesUP.enums.Category;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference("author-games")
    private Author author;


    @Enumerated(EnumType.STRING)
    private Category category;


    public Game() {}
    public Game(String title) { this.title = title; }

    public Long getId() { return id; }
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) {
        this.author = author ; 
    }

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @JsonBackReference("publisher-games")
    private Publisher publisher;

    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Avis> avisList;


    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public void setId(Long id) {
        this.id = id;
    }
    }
