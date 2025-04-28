package com.example.cricket_app.dto.response;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayoutResponse {
    BigDecimal payoutPerWinner;
    LocalDateTime processed_at;
    Integer matchId;
    Integer userId;
}
