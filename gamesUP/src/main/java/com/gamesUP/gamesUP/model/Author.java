package com.gamesUP.gamesUP.model;



import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference("author-games")
    private List<Game> games;

    public Author() {}
    public Author(String name) { this.name = name; }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

}


