package com.example.cricket_app.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UpcomingMatchResponse {
    private List<MatchResponse> matches;
}
