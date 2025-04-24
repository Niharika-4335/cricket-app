package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.response.PagedWalletTransactionResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.mapper.WalletTransactionMapper;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.repository.WalletRepository;
import com.example.cricket_app.repository.WalletTransactionRepository;
import com.example.cricket_app.security.AuthUtils;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletTransactionImpl implements WalletTransactionService {

    private WalletRepository walletRepository;
    private MatchRepository matchRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private WalletTransactionMapper walletTransactionMapper;
    private UserRepository userRepository;

    @Autowired
    public WalletTransactionImpl(WalletRepository walletRepository, MatchRepository matchRepository, WalletTransactionRepository walletTransactionRepository, WalletTransactionMapper walletTransactionMapper, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.matchRepository = matchRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionMapper = walletTransactionMapper;
        this.userRepository = userRepository;

    }

    @Override
    public PagedWalletTransactionResponse getTransactionsByUserId(int page, int size, String sortBy, String direction) {
        int pageNumber = Math.max(0, page - 1);
        int pageSize = Math.max(1, size);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        Long userId = AuthUtils.getLoggedInUserId();
        Page<WalletTransaction> transactions = walletTransactionRepository
                .findByWallet_User_IdOrderByCreatedAtDesc(userId, pageable);

        List<WalletTransactionResponse> transactionDtos = transactions.map(walletTransactionMapper::toResponseDto).getContent();
        return new PagedWalletTransactionResponse(
                transactionDtos,//object
                transactions.getNumber() + 1,
                transactions.getTotalPages(),
                transactions.getTotalElements()//they came from page<WalletTransaction> transactions .
        );
    }


}
