package com.gamesUP.gamesUP.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);


}