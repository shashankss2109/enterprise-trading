package com.enterprise.trading.controller;

import com.enterprise.trading.model.User;
import com.enterprise.trading.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public AuthController(UserRepo userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {

        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return "redirect:/register?error";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/login";
    }
}