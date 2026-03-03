package com.enterprise.trading.model;

import jakarta.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance = 100000.0;

    @OneToOne
    private User user;

    public Account() {}

    public Account(User user) {
        this.user = user;
    }

    public Long getId() { return id; }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    public User getUser() { return user; }
}