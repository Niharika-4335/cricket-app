package com.example.cricket_app.dto.request;

import com.example.cricket_app.enums.Team;
import lombok.Data;

@Data
public class BetRequest {
    private Long userId;
    private Long matchId;
    private Team teamChosen;
}
