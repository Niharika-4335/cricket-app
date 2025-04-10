package com.example.cricket_app.controller;


import com.example.cricket_app.dto.request.CreditWalletRequest;
import com.example.cricket_app.dto.response.WalletResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/credit")
    public ResponseEntity<WalletResponse> creditWallet(@Valid @RequestBody CreditWalletRequest request) {
        WalletResponse response = walletService.creditWallet(request);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/user/{userId}")
//    public List<WalletTransactionResponse> getTransactionsByUserId(@PathVariable Long userId) {
//        return walletService.getTransactionsByUserId(userId);
//    }

}
