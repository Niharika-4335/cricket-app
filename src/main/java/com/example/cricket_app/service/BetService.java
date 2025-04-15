package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.dto.response.BetResponse;

import java.util.List;

public interface BetService {

    public void placeBet(BetRequest request);

    public List<BetResponse> getUserBetHistory(Long userId);

    public void updateBetStatusesForMatchWinner(Long matchId);
}
