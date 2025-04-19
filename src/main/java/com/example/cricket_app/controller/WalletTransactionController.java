package com.example.cricket_app.controller;

import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletTransactionController {

    private WalletTransactionService walletTransactionService;

    @Autowired
    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }


    @PreAuthorize("hasRole('PLAYER')")
    @GetMapping("/transaction")
    public ResponseEntity<List<WalletTransactionResponse>> getTransactionHistory() {
        List<WalletTransactionResponse> response = walletTransactionService.getTransactionsByUserId();
        return ResponseEntity.ok(response);
    }

}
