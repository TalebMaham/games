package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByName(String name);
}
