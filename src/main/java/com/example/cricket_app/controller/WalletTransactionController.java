package com.example.cricket_app.controller;

import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;

    @Autowired
    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }


    @GetMapping("/transaction/{userId}")
    public ResponseEntity<List<WalletTransactionResponse>> getTransactionHistory(@PathVariable Long userId) {
        List<WalletTransactionResponse> response = walletTransactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

}
