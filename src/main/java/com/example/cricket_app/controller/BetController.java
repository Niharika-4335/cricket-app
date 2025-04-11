package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bet")
public class BetController {
    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> placeBet(@RequestBody BetRequest request) {
        betService.placeBet(request);
        return ResponseEntity.ok("Bet placed successfully.");
    }
}
