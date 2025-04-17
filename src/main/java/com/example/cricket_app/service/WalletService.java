package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.CreateWalletRequest;
import com.example.cricket_app.dto.request.CreditWalletRequest;
import com.example.cricket_app.dto.response.WalletResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.Users;

import java.util.List;

public interface WalletService {


     WalletResponse initializeWallet(CreateWalletRequest request);
     WalletResponse creditWallet(CreditWalletRequest creditWalletRequest);
     WalletResponse viewCurrentBalance();

}
