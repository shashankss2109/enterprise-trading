package com.enterprise.trading.model;

import jakarta.persistence.*;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private int quantity;
    private double avgCost;
    private double realizedProfit;

    @ManyToOne
    private User user;

    public Position() {}

    public Position(String symbol, User user) {
        this.symbol = symbol;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getAvgCost() { return avgCost; }
    public double getRealizedProfit() { return realizedProfit; }
    public User getUser() { return user; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setAvgCost(double avgCost) { this.avgCost = avgCost; }
    public void setRealizedProfit(double realizedProfit) { this.realizedProfit = realizedProfit; }
}