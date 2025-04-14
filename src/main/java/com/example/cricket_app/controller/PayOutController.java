package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.DeclareWinnerRequest;
import com.example.cricket_app.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matches")
public class PayOutController {
    private final MatchService matchService;

    @Autowired
    public PayOutController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/declare-winner")
    public String declareMatchWinner(@RequestBody DeclareWinnerRequest request) {
        matchService.declareMatchWinner(request.getMatchId(), String.valueOf(request.getWinningTeam()));
        return "Match winner declared and payouts processed.";
    }
}
