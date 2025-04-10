package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.CreditWalletRequest;
import com.example.cricket_app.dto.request.WalletTransactionRequest;
import com.example.cricket_app.dto.response.WalletResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.service.WalletTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletTransactionController {

   private  WalletTransactionService walletTransactionService;

   @Autowired
    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<WalletTransactionResponse> creditWallet(@Valid @RequestBody WalletTransactionRequest request) {
        WalletTransactionResponse response = walletTransactionService.createTransaction(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public List<WalletTransactionResponse> getTransactionsByUserId(@PathVariable Long userId) {
        return walletTransactionService.getTransactionsByUserId(userId);
    }
}
