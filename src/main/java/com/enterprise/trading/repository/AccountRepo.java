package com.enterprise.trading.repository;

import com.enterprise.trading.model.Account;
import com.enterprise.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByUser(User user);
}