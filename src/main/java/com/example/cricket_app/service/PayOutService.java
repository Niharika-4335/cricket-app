package com.example.cricket_app.service;

import com.example.cricket_app.dto.response.PagedPayOutSummaryResponse;
import com.example.cricket_app.dto.response.PayOutSummaryResponse;
import com.example.cricket_app.entity.Match;

public interface PayOutService {
    PayOutSummaryResponse processPayout(Long matchId);
}
