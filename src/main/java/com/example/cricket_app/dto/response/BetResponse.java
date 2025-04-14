package com.example.cricket_app.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BetResponse {

    private Long betId;
    private Long matchId;
    private String teamChosen;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;

}
