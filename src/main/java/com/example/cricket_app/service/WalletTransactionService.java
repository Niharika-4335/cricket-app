package com.example.cricket_app.service;

import com.example.cricket_app.enums.TransactionType;

import java.math.BigDecimal;

public interface WalletTransactionService {

    public void creditToWallet(Long userId, BigDecimal amount, String description, TransactionType type, Long matchId);

    public void debitFromWallet(Long userId, BigDecimal amount, String description, Long matchId);
}
