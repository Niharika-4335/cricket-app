package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.PastMatchesResultResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface MatchService {
    public MatchResponse createMatch(CreateMatchRequest createMatchRequest) throws BadRequestException;

    public UpcomingMatchResponse getUpcomingMatches();

    public MatchResponse getMatchById(Long id);

    public void declareMatchWinner(Long matchId, String winningTeam);

    public List<PastMatchesResultResponse> viewPastMatchesResults();

    }
