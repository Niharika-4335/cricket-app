package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.DeclareWinnerRequest;
import com.example.cricket_app.dto.response.PayOutSummaryResponse;
import com.example.cricket_app.service.MatchService;
import com.example.cricket_app.service.PayOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class PayOutController {
    private final MatchService matchService;
    private final PayOutService payOutService;

    @Autowired
    public PayOutController(MatchService matchService, PayOutService payOutService) {
        this.matchService = matchService;
        this.payOutService = payOutService;
    }

    @PostMapping("/declare-winner")
    public String declareMatchWinner(@RequestBody DeclareWinnerRequest request) {
        matchService.declareMatchWinner(request.getMatchId(), String.valueOf(request.getWinningTeam()));
        return "Match winner declared and payouts processed.";
    }
//    @GetMapping("/payoutSummary/{matchId}")
//    public PayOutSummaryResponse getPayoutSummary(@PathVariable Long matchId) {
//        return payOutService.processPayout(matchId);
//    }


}
