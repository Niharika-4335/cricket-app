package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.dto.response.BetResponse;
import com.example.cricket_app.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bet")
public class BetController {
    private BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/create")
    public ResponseEntity<BetResponse> placeBet(@RequestBody BetRequest request) {
        BetResponse response = betService.placeBet(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PLAYER')")
    @GetMapping("/history")
    public ResponseEntity<List<BetResponse>> getBetHistory() {
        List<BetResponse> response = betService.getUserBetHistory();
        return ResponseEntity.ok(response);
    }
}
