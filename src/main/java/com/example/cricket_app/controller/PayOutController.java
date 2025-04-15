package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.DeclareWinnerRequest;
import com.example.cricket_app.dto.response.PayOutSummaryResponse;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.service.MatchService;
import com.example.cricket_app.service.PayOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class PayOutController {
    private final MatchService matchService;
    private final PayOutService payOutService;
    private final MatchRepository matchRepository;

    @Autowired
    public PayOutController(MatchService matchService, PayOutService payOutService, MatchRepository matchRepository) {
        this.matchService = matchService;
        this.payOutService = payOutService;
        this.matchRepository = matchRepository;
    }

    @PostMapping("/declare-winner")
    public String declareMatchWinner(@RequestBody DeclareWinnerRequest request) {
        matchService.declareMatchWinner(request.getMatchId(), String.valueOf(request.getWinningTeam()));
        return "Match winner declared and payouts processed.";
    }

    @GetMapping("/payoutSummary/{matchId}")
    public ResponseEntity<PayOutSummaryResponse> getPayoutSummary(@PathVariable Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        PayOutSummaryResponse response = payOutService.processPayout(match);
        return ResponseEntity.ok(response);
    }


}
