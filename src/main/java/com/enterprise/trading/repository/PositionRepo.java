package com.enterprise.trading.repository;

import com.enterprise.trading.model.Position;
import com.enterprise.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepo extends JpaRepository<Position, Long> {
    List<Position> findByUser(User user);
    Optional<Position> findByUserAndSymbol(User user, String symbol);
}