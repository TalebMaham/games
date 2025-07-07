package com.gamesUP.gamesUP.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {}
