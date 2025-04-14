package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.dto.response.BetResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<BetResponse>> getTransactionHistory(@PathVariable Long userId) {
        List<BetResponse> response = betService.getUserBetHistory(userId);
        return ResponseEntity.ok(response);
    }
}
