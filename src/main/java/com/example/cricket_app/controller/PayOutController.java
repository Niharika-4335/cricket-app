package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.DeclareWinnerRequest;
import com.example.cricket_app.dto.response.PayOutSummaryResponse;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.service.MatchService;
import com.example.cricket_app.service.PayOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payout")
public class PayOutController {
    private MatchService matchService;
    private PayOutService payOutService;
    private MatchRepository matchRepository;

    @Autowired
    public PayOutController(MatchService matchService, PayOutService payOutService, MatchRepository matchRepository) {
        this.matchService = matchService;
        this.payOutService = payOutService;
        this.matchRepository = matchRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/declare-winner")
    public String declareMatchWinner(@RequestBody DeclareWinnerRequest request) {
        matchService.declareMatchWinner(request.getMatchId(), String.valueOf(request.getWinningTeam()));
        return "Match winner declared and payouts processed.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/payoutSummary/{matchId}")
    public ResponseEntity<PayOutSummaryResponse> getPayoutSummary(@PathVariable Long matchId) {

        PayOutSummaryResponse response = payOutService.processPayout(matchId);
        return ResponseEntity.ok(response);
    }


}
