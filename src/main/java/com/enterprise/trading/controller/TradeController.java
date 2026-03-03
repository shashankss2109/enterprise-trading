package com.enterprise.trading.controller;

import com.enterprise.trading.MarketEngine;
import com.enterprise.trading.service.TradeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TradeController {

    private final TradeService tradeService;
    private final MarketEngine marketEngine;

    public TradeController(TradeService tradeService, MarketEngine marketEngine) {
        this.tradeService = tradeService;
        this.marketEngine = marketEngine;
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication auth) {

        String username = auth.getName();

        model.addAttribute("prices", marketEngine.getPrices());
        model.addAttribute("positions", tradeService.getPositions(username));
        model.addAttribute("orders", tradeService.getOrders(username));
        model.addAttribute("balance", tradeService.getBalance(username));
        model.addAttribute("totalValue", tradeService.getPortfolioValue(username));
        model.addAttribute("service", tradeService);

        return "index";
    }

    @PostMapping("/trade")
    public String trade(Authentication auth,
                        @RequestParam String symbol,
                        @RequestParam int qty,
                        @RequestParam String type) {

        tradeService.executeTrade(auth.getName(), symbol, qty, type);
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(Authentication auth, @RequestParam double amount) {
        tradeService.deposit(auth.getName(), amount);
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(Authentication auth, @RequestParam double amount) {
        tradeService.withdraw(auth.getName(), amount);
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String reset(Authentication auth) {
        tradeService.resetAccount(auth.getName());
        return "redirect:/";
    }
}