package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUserId(Long userId);
    List<Purchase> findByUserEmail(String email);
}

