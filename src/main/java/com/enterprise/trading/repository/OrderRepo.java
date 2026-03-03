package com.enterprise.trading.repository;

import com.enterprise.trading.model.TradeOrder;
import com.enterprise.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<TradeOrder, Long> {
    List<TradeOrder> findByUserOrderByTimeDesc(User user);
}