package com.gamesUP.gamesUP.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repositories.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository repo;
    public AuthorService(AuthorRepository repo) { this.repo = repo; }

    public Author create(String name) {
        return repo.save(new Author(name));
    }

    public List<Author> getAll() {
        return repo.findAll();
    }
}

