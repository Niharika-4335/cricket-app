package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.WalletTransactionRequest;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.Wallet;
import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.mapper.WalletTransactionMapper;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.repository.WalletRepository;
import com.example.cricket_app.repository.WalletTransactionRepository;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private UserRepository userRepository;
    private WalletTransactionMapper walletTransactionMapper;
    private WalletRepository walletRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private MatchRepository matchRepository;

    @Autowired
    public WalletTransactionServiceImpl(UserRepository userRepository, WalletTransactionMapper walletTransactionMapper, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.walletTransactionMapper = walletTransactionMapper;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public WalletTransactionResponse createTransaction(WalletTransactionRequest requestDTO) {
        Users user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user"));


        WalletTransaction transaction = walletTransactionMapper.toEntity(requestDTO);

        transaction.setWallet(wallet);
        if(requestDTO.getMatchId() != null) {
            Match match = matchRepository.findById(requestDTO.getMatchId())
                    .orElseThrow(() -> new RuntimeException("Match not found"));
            transaction.setMatch(match);  // Set the match object, not the ID
        }

        WalletTransaction savedTransaction = walletTransactionRepository.save(transaction);
        return walletTransactionMapper.toResponse(savedTransaction);
    }

    @Override
    public List<WalletTransactionResponse> getTransactionsByUserId(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user"));

        List<WalletTransaction> transactions = walletTransactionRepository.findByWallet(wallet);

        return walletTransactionMapper.toResponseList(transactions);
    }
}
