package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.PastMatchesResultResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.enums.Team;
import com.example.cricket_app.exception.*;
import com.example.cricket_app.mapper.MatchMapper;
import com.example.cricket_app.mapper.PastMatchesResultMapper;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.service.BetService;
import com.example.cricket_app.service.MatchService;
import com.example.cricket_app.service.PayOutService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    private MatchRepository matchRepository;
    private MatchMapper matchMapper;
    private PayOutService payOutService;
    private PastMatchesResultMapper pastMatchesResultMapper;
    private BetService betService;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, MatchMapper matchMapper, PayOutService payOutService, PastMatchesResultMapper pastMatchesResultMapper, BetService betService) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.payOutService = payOutService;
        this.pastMatchesResultMapper = pastMatchesResultMapper;
        this.betService = betService;
    }

    @Override
    @Transactional
    public MatchResponse createMatch(CreateMatchRequest createMatchRequest) throws BadRequestException {
        try {
            Team.valueOf(createMatchRequest.getTeamA().toUpperCase());
            Team.valueOf(createMatchRequest.getTeamB().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTeamChosenException("Invalid team name. Must be one of: " + Arrays.toString(Team.values()));
        }

        if (createMatchRequest.getTeamA().equalsIgnoreCase(createMatchRequest.getTeamB())) {
            throw new SameTeamSelectionException("Teams cannot be the same");
        }

        // if match start time is before(passed time) the current time.
        if (createMatchRequest.getStartTime().isBefore(LocalDateTime.now())) {
            throw new MatchStartTimeInPastException("Match start time must be in the future");
        }

        Match match = matchMapper.toEntity(createMatchRequest);
        Match savedMatch = matchRepository.save(match);

        MatchResponse matchResponse = matchMapper.toResponseDto(savedMatch);
        return matchResponse;
    }

    @Override
    public Page<UpcomingMatchResponse> getUpcomingMatches(Pageable pageable) {
        Page<Match> matches = matchRepository.findUpcomingMatches(LocalDateTime.now(), pageable);
        List<UpcomingMatchResponse> upcomingMatchResponses = new ArrayList<>();

        for (Match match : matches.getContent()) {
            MatchResponse matchResponse = matchMapper.toResponseDto(match);
            UpcomingMatchResponse upcomingMatchResponse = new UpcomingMatchResponse();
            upcomingMatchResponse.setMatches(Collections.singletonList(matchResponse));
            upcomingMatchResponses.add(upcomingMatchResponse);
        }
        return new PageImpl<>(upcomingMatchResponses, pageable, matches.getTotalElements());

    }

    @Override
    public MatchResponse getMatchById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException("match not found"));

        return matchMapper.toResponseDto(match);
    }

    @Override
    public void declareMatchWinner(Long matchId, String winningTeam) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        if (match.getWinningTeam() != null) {
            throw new WinnerAlreadyDeclaredException("Winner already declared for this match.");
        }

        if (match.getStartTime().isAfter(LocalDateTime.now())) {
            throw new MatchNotStartedException("Cannot declare winner before match starts.");
        }
        Team winningTeamEnum = Team.valueOf(winningTeam); //valueOf converts String to Enum
        match.setWinningTeam(winningTeamEnum);
        match.setStatus(MatchStatus.COMPLETED);
        matchRepository.save(match);
        betService.updateBetStatusesForMatchWinner(matchId);
        payOutService.processPayout(matchId);

    }

    @Override
    public List<PastMatchesResultResponse> viewPastMatchesResults() {
        List<Match> matches = matchRepository.findAll().stream()
                .filter(m -> m.getStatus() == MatchStatus.COMPLETED).collect(Collectors.toUnmodifiableList());

        return pastMatchesResultMapper.toResponseDtoList(matches);//returning completed matches as lists
    }


    @Scheduled(fixedRate = 60000)//in milliseconds=1 min
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
        }//to change the status if time passed from the present time now.

//        List<Match> toComplete = matchRepository.findAll().stream()
//                .filter(m -> m.getStatus() == MatchStatus.ONGOING && m.getStartTime().plusMinutes(5).isBefore(now))
//                .collect(Collectors.toList());
//
//        for (Match match : toComplete) {
//            match.setStatus(MatchStatus.COMPLETED);
//            match.setUpdatedAt(now);
//            matchRepository.save(match);
//        }//after 5 minutes...match will be completed.

    }


}
