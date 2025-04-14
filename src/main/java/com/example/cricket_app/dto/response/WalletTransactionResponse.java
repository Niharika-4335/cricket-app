package com.example.cricket_app.dto.response;

import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletTransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String transactionType;
    private String description;
    private LocalDateTime createdAt;
    private Long matchId;
    private Long userId;
}
