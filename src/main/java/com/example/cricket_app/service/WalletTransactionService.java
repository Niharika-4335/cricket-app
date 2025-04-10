package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.WalletTransactionRequest;
import com.example.cricket_app.dto.response.WalletTransactionResponse;

import java.util.List;

public interface WalletTransactionService {

    public WalletTransactionResponse createTransaction(WalletTransactionRequest requestDTO);

    public List<WalletTransactionResponse> getTransactionsByUserId(Long userId);
}
