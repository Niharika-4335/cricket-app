package com.example.cricket_app.dto.response;

import com.example.cricket_app.entity.Match;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.enums.Team;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MatchResponse {
    private Long id;
    private String teamA;
    private String teamB;
    private LocalDateTime startTime;
    private BigDecimal betAmount;
    private MatchStatus status;
    private Team winningTeam;


}
