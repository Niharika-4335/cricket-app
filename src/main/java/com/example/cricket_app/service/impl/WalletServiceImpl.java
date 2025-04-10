package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.CreditWalletRequest;
import com.example.cricket_app.dto.response.WalletResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.Wallet;
import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.mapper.WalletMapper;
import com.example.cricket_app.mapper.WalletTransactionMapper;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.repository.WalletRepository;
import com.example.cricket_app.repository.WalletTransactionRepository;
import com.example.cricket_app.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private WalletMapper walletMapper;
    private WalletRepository walletRepository;
    private UserRepository userRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private WalletTransactionMapper walletTransactionMapper;


    @Autowired
    public WalletServiceImpl(WalletMapper walletMapper, WalletRepository walletRepository, UserRepository userRepository, WalletTransactionRepository walletTransactionRepository, WalletTransactionMapper walletTransactionMapper) {
        this.walletMapper = walletMapper;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionMapper = walletTransactionMapper;
    }

    @Override
    public WalletResponse creditWallet(CreditWalletRequest creditWalletRequest) {
        if (!userRepository.existsById(creditWalletRequest.getUserId())) {
         throw new RuntimeException("user id not found");
      }

        if (creditWalletRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
           throw new RuntimeException("amount must be positive");
      }

        Users user = userRepository.findById(creditWalletRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
       Wallet wallet = walletRepository.findById(creditWalletRequest.getUserId())
               .orElseGet(() -> {
                    Wallet newWallet=walletMapper.toEntity(creditWalletRequest,user);
                     return walletRepository.save(newWallet);

                });
       return walletMapper.toResponseDto(wallet);
    }



}
