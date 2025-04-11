package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.entity.Bet;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.enums.BetStatus;
import com.example.cricket_app.repository.BetRepository;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.service.BetService;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BetServiceImpl implements BetService {
    private UserRepository userRepository;
    private MatchRepository matchRepository;
    private BetRepository betRepository;
    private WalletTransactionService walletTransactionService;

    @Autowired
    public BetServiceImpl(UserRepository userRepository, BetRepository betRepository, MatchRepository matchRepository, WalletTransactionService walletTransactionService) {
        this.userRepository = userRepository;
        this.betRepository = betRepository;
        this.matchRepository = matchRepository;
        this.walletTransactionService = walletTransactionService;
    }

    @Override
    public void placeBet(BetRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (betRepository.existsByUserAndMatch(user, match)) {
            throw new RuntimeException("User already placed a bet for this match.");
        }

        BigDecimal betAmount = match.getBetAmount(); // get amount from match

        walletTransactionService.debitFromWallet(user.getId(), betAmount,
                "Bet placed on match " + match.getId(), match.getId());

        Bet bet = new Bet();
        bet.setUser(user);
        bet.setMatch(match);
        bet.setTeamChosen(request.getTeamChosen());
        bet.setAmount(betAmount);
        bet.setStatus(BetStatus.PENDING);
        betRepository.save(bet);
    }
}
