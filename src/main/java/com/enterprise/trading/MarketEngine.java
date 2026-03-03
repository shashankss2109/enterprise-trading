package com.enterprise.trading;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketEngine {

    private final Map<String, Double> prices = new ConcurrentHashMap<>();

    public MarketEngine() {
        prices.put("AAPL", 185.0);
        prices.put("TSLA", 210.0);
        prices.put("NVDA", 820.0);
        prices.put("BTC", 64000.0);
    }

    @Scheduled(fixedRate = 2000)
    public void updatePrices() {
        prices.replaceAll((k, v) -> v + (Math.random() - 0.5) * 4);
    }

    public Map<String, Double> getPrices() {
        return prices;
    }

    public double getPrice(String symbol) {
        return prices.getOrDefault(symbol, 0.0);
    }
}