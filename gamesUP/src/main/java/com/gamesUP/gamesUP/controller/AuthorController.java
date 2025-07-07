package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.services.AuthorService;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService service;
    public AuthorController(AuthorService service) { this.service = service; }

    @PostMapping
    public Author create(@RequestParam String name) {
        return service.create(name);
    }

    @GetMapping
    public List<Author> getAll() {
        return service.getAll();
    }
}

