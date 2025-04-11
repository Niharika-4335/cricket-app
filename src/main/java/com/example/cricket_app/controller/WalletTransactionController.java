package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.WalletTransactionRequest;
import com.example.cricket_app.enums.TransactionType;
import com.example.cricket_app.service.WalletService;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;

    @Autowired
    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> addWalletTransaction(@RequestBody WalletTransactionRequest request) {
        if (request.getTransactionType() == TransactionType.BET_PLACED) {
            walletTransactionService.debitFromWallet(
                    request.getUserId(),
                    request.getAmount(),
                    request.getDescription(),
                    request.getMatchId()
            );
        } else {
            walletTransactionService.creditToWallet(
                    request.getUserId(),
                    request.getAmount(),
                    request.getDescription(),
                    request.getTransactionType(),
                    request.getMatchId()
            );
        }
        return ResponseEntity.ok("Transaction successful");
    }
}
