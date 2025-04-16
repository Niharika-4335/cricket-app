package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.dto.response.BetResponse;

import java.util.List;

public interface BetService {

     void placeBet(BetRequest request);

     List<BetResponse> getUserBetHistory();

     void updateBetStatusesForMatchWinner(Long matchId);
}
