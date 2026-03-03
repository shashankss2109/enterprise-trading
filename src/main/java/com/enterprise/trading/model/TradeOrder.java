package com.enterprise.trading.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TradeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String type;
    private int quantity;
    private double price;
    private LocalDateTime time;

    @ManyToOne
    private User user;

    public TradeOrder() {}

    public TradeOrder(String symbol, String type, int quantity, double price, User user) {
        this.symbol = symbol;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.time = LocalDateTime.now();
        this.user = user;
    }

    public Long getId() { return id; }
    public String getSymbol() { return symbol; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getTime() { return time; }
}