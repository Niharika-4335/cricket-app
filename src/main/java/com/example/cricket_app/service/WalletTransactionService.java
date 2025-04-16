package com.example.cricket_app.service;

import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface WalletTransactionService {

     void creditToWallet(Long userId, BigDecimal amount, String description, TransactionType type, Long matchId);

     void debitFromWallet(Long userId, BigDecimal amount, String description, Long matchId);

     List<WalletTransactionResponse> getTransactionsByUserId();
}
