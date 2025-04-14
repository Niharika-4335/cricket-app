package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Wallet;
import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.enums.TransactionType;
import com.example.cricket_app.exception.InsufficientBalanceException;
import com.example.cricket_app.exception.MatchNotFoundException;
import com.example.cricket_app.exception.WalletNotFoundException;
import com.example.cricket_app.mapper.WalletTransactionMapper;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.repository.WalletRepository;
import com.example.cricket_app.repository.WalletTransactionRepository;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletTransactionImpl implements WalletTransactionService {

    private WalletRepository walletRepository;
    private MatchRepository matchRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private WalletTransactionMapper walletTransactionMapper;
    private UserRepository userRepository;

    public WalletTransactionImpl(WalletRepository walletRepository, MatchRepository matchRepository, WalletTransactionRepository walletTransactionRepository, WalletTransactionMapper walletTransactionMapper, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.matchRepository = matchRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionMapper = walletTransactionMapper;
        this.userRepository = userRepository;
    }


    @Override
    public void creditToWallet(Long userId, BigDecimal amount, String description, TransactionType type, Long matchId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user " + userId));

        Match match = null;
        if (matchId != null) {
            match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found with ID " + matchId));
        }

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setDescription(description);
        transaction.setMatch(match);
        walletTransactionRepository.save(transaction);
    }

    @Override
    public void debitFromWallet(Long userId, BigDecimal amount, String description, Long matchId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user " + userId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance!");
        }

        Match match = null;
        if (matchId != null) {
            match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new MatchNotFoundException("Match not found with ID " + matchId));
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.BET_PLACED);
        transaction.setDescription(description);
        transaction.setMatch(match); // Set match if there
        walletTransactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransactionResponse> getTransactionsByUserId(Long userId) {
        List<WalletTransaction> transactions = walletTransactionRepository
                .findByWallet_User_IdOrderByCreatedAtDesc(userId);

        return walletTransactionMapper.toResponseDtoList(transactions);
    }


}
