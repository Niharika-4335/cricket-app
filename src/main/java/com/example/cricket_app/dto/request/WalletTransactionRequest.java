package com.example.cricket_app.dto.request;

import com.example.cricket_app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletTransactionRequest {

    private Long userId;
    private BigDecimal amount;
    private String description;
    private TransactionType transactionType;
    private Long matchId;
}
