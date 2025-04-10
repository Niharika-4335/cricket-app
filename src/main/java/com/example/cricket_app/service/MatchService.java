package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import org.apache.coyote.BadRequestException;

public interface MatchService {
    public MatchResponse createMatch(CreateMatchRequest createMatchRequest) throws BadRequestException;

    public UpcomingMatchResponse getUpcomingMatches();

    public MatchResponse getMatchById(Long id);
}
