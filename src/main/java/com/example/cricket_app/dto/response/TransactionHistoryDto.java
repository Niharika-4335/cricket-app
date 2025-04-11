package com.example.cricket_app.dto.response;

import com.example.cricket_app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionHistoryDto {

    private Long id;
    private Long userId;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private TransactionType transactionType;
    private Long matchId;
    private String description;
    private LocalDateTime timestamp;
}
