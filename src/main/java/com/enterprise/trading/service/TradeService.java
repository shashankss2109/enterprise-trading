package com.enterprise.trading.service;

import com.enterprise.trading.MarketEngine;
import com.enterprise.trading.model.*;
import com.enterprise.trading.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TradeService {

    private final PositionRepo positionRepo;
    private final OrderRepo orderRepo;
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final MarketEngine marketEngine;

    private static final double FEE_RATE = 0.001; // 0.1%

    public TradeService(PositionRepo positionRepo,
                        OrderRepo orderRepo,
                        AccountRepo accountRepo,
                        UserRepo userRepo,
                        MarketEngine marketEngine) {
        this.positionRepo = positionRepo;
        this.orderRepo = orderRepo;
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.marketEngine = marketEngine;
    }

    private User getUser(String username) {
        return userRepo.findByUsername(username).orElseThrow();
    }

    private Account getAccount(User user) {
        return accountRepo.findByUser(user)
                .orElseGet(() -> accountRepo.save(new Account(user)));
    }

    public void deposit(String username, double amount) {
        User user = getUser(username);
        Account acc = getAccount(user);
        acc.setBalance(acc.getBalance() + amount);
    }

    public void withdraw(String username, double amount) {
        User user = getUser(username);
        Account acc = getAccount(user);
        if (acc.getBalance() >= amount)
            acc.setBalance(acc.getBalance() - amount);
    }

    public void executeTrade(String username, String symbol, int qty, String type) {

        User user = getUser(username);
        Account acc = getAccount(user);

        double price = marketEngine.getPrice(symbol);
        double total = price * qty;
        double fee = total * FEE_RATE;

        Position pos = positionRepo.findByUserAndSymbol(user, symbol)
                .orElse(new Position(symbol, user));

        if (type.equalsIgnoreCase("BUY")) {

            if (acc.getBalance() >= total + fee) {

                double newQty = pos.getQuantity() + qty;
                double newAvg = ((pos.getQuantity() * pos.getAvgCost()) + total) / newQty;

                pos.setQuantity((int) newQty);
                pos.setAvgCost(newAvg);

                acc.setBalance(acc.getBalance() - total - fee);
            }

        } else {

            if (pos.getQuantity() >= qty) {

                double profit = qty * (price - pos.getAvgCost());
                pos.setRealizedProfit(pos.getRealizedProfit() + profit);
                pos.setQuantity(pos.getQuantity() - qty);

                acc.setBalance(acc.getBalance() + total - fee);
            }
        }

        positionRepo.save(pos);
        orderRepo.save(new TradeOrder(symbol, type, qty, price, user));
    }

    public List<Position> getPositions(String username) {
        return positionRepo.findByUser(getUser(username));
    }

    public List<TradeOrder> getOrders(String username) {
        return orderRepo.findByUserOrderByTimeDesc(getUser(username));
    }

    public double getBalance(String username) {
        return getAccount(getUser(username)).getBalance();
    }

    public double getPortfolioValue(String username) {

        User user = getUser(username);
        double total = getBalance(username);

        for (Position p : positionRepo.findByUser(user)) {
            total += p.getQuantity() * marketEngine.getPrice(p.getSymbol());
        }

        return total;
    }

    public double getUnrealizedProfit(Position p) {
        double current = marketEngine.getPrice(p.getSymbol());
        return p.getQuantity() * (current - p.getAvgCost());
    }

    public void resetAccount(String username) {
        User user = getUser(username);
        positionRepo.deleteAll(positionRepo.findByUser(user));
        orderRepo.deleteAll(orderRepo.findByUserOrderByTimeDesc(user));
        Account acc = getAccount(user);
        acc.setBalance(100000.0);
    }
}