package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.PastMatchesResultResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface MatchService {
     MatchResponse createMatch(CreateMatchRequest createMatchRequest) throws BadRequestException;

     UpcomingMatchResponse getUpcomingMatches();

     MatchResponse getMatchById(Long id);

     void declareMatchWinner(Long matchId, String winningTeam);

     List<PastMatchesResultResponse> viewPastMatchesResults();

    }
