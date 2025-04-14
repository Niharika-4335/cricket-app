package com.example.cricket_app.dto.response;


import com.example.cricket_app.enums.Team;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PastMatchesResultResponse {
    private Long id;
    private String teamA;
    private String teamB;
    private BigDecimal betAmount;
    private Team winningTeam;
    private LocalDateTime startTime;

}
