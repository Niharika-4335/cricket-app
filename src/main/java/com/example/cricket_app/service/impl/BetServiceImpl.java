package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.dto.response.BetResponse;
import com.example.cricket_app.entity.Bet;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.enums.BetStatus;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.exception.DuplicateBetException;
import com.example.cricket_app.exception.MatchNotFoundException;
import com.example.cricket_app.exception.UserNotFoundException;
import com.example.cricket_app.mapper.BetMapper;
import com.example.cricket_app.repository.BetRepository;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.security.AuthUtils;
import com.example.cricket_app.service.BetService;
import com.example.cricket_app.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BetServiceImpl implements BetService {
    private UserRepository userRepository;
    private MatchRepository matchRepository;
    private BetRepository betRepository;
    private WalletTransactionService walletTransactionService;
    private BetMapper betMapper;

    @Autowired
    public BetServiceImpl(UserRepository userRepository, MatchRepository matchRepository, BetRepository betRepository, WalletTransactionService walletTransactionService, BetMapper betMapper) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
        this.walletTransactionService = walletTransactionService;
        this.betMapper = betMapper;
    }

    @Override
    public void placeBet(BetRequest request) {
        Long userId = AuthUtils.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        if (betRepository.existsByUserAndMatch(user, match)) {
            throw new DuplicateBetException("User already placed a bet for this match.");
        }

        if (match.getStatus() == MatchStatus.ONGOING || match.getStatus() == MatchStatus.COMPLETED) {
            throw new IllegalStateException("Bets are not allowed after the match has started.");
        }

        BigDecimal betAmount = match.getBetAmount();

        walletTransactionService.debitFromWallet(user.getId(), betAmount,
                "Bet placed on match " + match.getId(), match.getId());

        Bet bet = new Bet();
        bet.setUser(user);//passing the whole object because we cant only pass the user_id.
        bet.setMatch(match);
        bet.setTeamChosen(request.getTeamChosen());
        bet.setAmount(betAmount);
        bet.setStatus(BetStatus.PENDING);
        bet.setCreatedAt(LocalDateTime.now());
        betRepository.save(bet);
    }


    @Override
    public void updateBetStatusesForMatchWinner(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        if (match.getWinningTeam() == null) {
            throw new IllegalStateException("Match winner is not declared yet.");
        }

        List<Bet> bets = betRepository.findByMatch_Id(matchId);

        for (Bet bet : bets) {
            if (bet.getTeamChosen().equals(match.getWinningTeam())) {
                bet.setStatus(BetStatus.WON);
            } else {
                bet.setStatus(BetStatus.LOST);
            }
        }

        betRepository.saveAll(bets);
    }

    @Override
    public List<BetResponse> getUserBetHistory() {
        Long userId = AuthUtils.getLoggedInUserId();
        List<Bet> bets = betRepository.findByUser_IdOrderByIdDesc(userId);
        return bets.stream()
                .map(betMapper::toResponse)//betMapper is an object and toResponse refers to the instance method.
                .collect(Collectors.toList());
    }
}
