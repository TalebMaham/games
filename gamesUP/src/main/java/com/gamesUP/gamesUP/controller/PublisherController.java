package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.services.PublisherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @PostMapping
    public Publisher create(@RequestParam String name) {
        return service.create(name);
    }

    @GetMapping
    public List<Publisher> getAll() {
        return service.getAll();
    }
}
