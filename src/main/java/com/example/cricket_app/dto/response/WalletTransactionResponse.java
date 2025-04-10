package com.example.cricket_app.dto.response;

import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletTransactionResponse {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Long matchId;
    private LocalDateTime  createdAt;
    private String description;
}
