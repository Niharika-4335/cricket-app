package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.mapper.MatchMapper;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.service.MatchService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    private MatchRepository matchRepository;
    private MatchMapper matchMapper;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
    }

    @Override
    @Transactional
    public MatchResponse createMatch(CreateMatchRequest createMatchRequest) throws BadRequestException {
        if (createMatchRequest.getTeamA().equalsIgnoreCase(createMatchRequest.getTeamB())) {
            throw new BadRequestException("Teams cannot be the same");
        }

        // Validate start time is in the future
        if (createMatchRequest.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Match start time must be in the future");
        }

        Match match = matchMapper.toEntity(createMatchRequest);
        Match savedMatch = matchRepository.save(match);

        MatchResponse matchResponse = matchMapper.toResponseDto(savedMatch);
        return matchResponse;
    }

    @Override
    public UpcomingMatchResponse getUpcomingMatches() {
        List<Match> matches = matchRepository.findUpcomingMatches(LocalDateTime.now());

        List<MatchResponse> matchResponses = new ArrayList<>();
        for (Match match : matches) {
            MatchResponse response = matchMapper.toResponseDto(match);
            matchResponses.add(response);
        }

        UpcomingMatchResponse response = new UpcomingMatchResponse();
        response.setMatches(matchResponses);

        return response;
    }

    @Override
    public MatchResponse getMatchById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("match not found"));

        return matchMapper.toResponseDto(match);
    }


    @Scheduled(fixedRate = 60000)
    public void updateMatchStatuses() {
        LocalDateTime now = LocalDateTime.now();
        // Find matches that are UPCOMING but should be ONGOING
        List<Match> matches = matchRepository.findAll().stream()
                .filter(m -> m.getStatus() == MatchStatus.UPCOMING && m.getStartTime().isBefore(now))
                .collect(Collectors.toList());

        for (Match match : matches) {
            match.setStatus(MatchStatus.ONGOING);
            match.setUpdatedAt(now);
            matchRepository.save(match);
        }
    }//here we are running this script to change the status if time passed from the present time now.


}
