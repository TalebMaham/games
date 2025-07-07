package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repositories.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository repository;

    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public Publisher create(String name) {
        if (repository.existsByName(name)) {
            throw new RuntimeException("Publisher already exists");
        }
        return repository.save(new Publisher(name));
    }

    public List<Publisher> getAll() {
        return repository.findAll();
    }
}
