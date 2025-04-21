package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.PastMatchesResultResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchService {
     MatchResponse createMatch(CreateMatchRequest createMatchRequest) throws BadRequestException;

     Page<UpcomingMatchResponse> getUpcomingMatches(Pageable pageable);

     MatchResponse getMatchById(Long id);

     void declareMatchWinner(Long matchId, String winningTeam);

     List<PastMatchesResultResponse> viewPastMatchesResults();

    }
