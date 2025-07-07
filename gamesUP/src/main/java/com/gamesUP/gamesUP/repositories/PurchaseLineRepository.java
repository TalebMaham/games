package com.gamesUP.gamesUP.repositories;
import com.gamesUP.gamesUP.model.PurchaseLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseLineRepository extends JpaRepository<PurchaseLine, Long> {
}
