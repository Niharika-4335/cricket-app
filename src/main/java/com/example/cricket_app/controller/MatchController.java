package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.dto.response.UpcomingMatchResponse;
import com.example.cricket_app.service.MatchService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/matches")
public class MatchController {
    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody CreateMatchRequest request) throws BadRequestException {
        MatchResponse createdMatch = matchService.createMatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMatch);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<UpcomingMatchResponse> getUpcomingMatches() {
        UpcomingMatchResponse response = matchService.getUpcomingMatches();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{match_id}")
    public ResponseEntity<MatchResponse> getUpcomingMatches(@PathVariable Long match_id) {
        MatchResponse response = matchService.getMatchById(match_id);
        return ResponseEntity.ok(response);
    }


}
