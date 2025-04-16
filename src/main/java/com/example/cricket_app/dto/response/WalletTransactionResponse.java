package com.example.cricket_app.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletTransactionResponse {
    private BigDecimal amount;
    private String transactionType;
    private String description;
    private LocalDateTime createdAt;
    private Long matchId;
    private Long userId;
}
