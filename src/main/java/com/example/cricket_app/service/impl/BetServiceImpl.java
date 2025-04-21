package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.BetRequest;
import com.example.cricket_app.dto.response.BetResponse;
import com.example.cricket_app.dto.response.PagedBetResponse;
import com.example.cricket_app.entity.Bet;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.Wallet;
import com.example.cricket_app.enums.BetStatus;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.exception.*;
import com.example.cricket_app.mapper.BetMapper;
import com.example.cricket_app.repository.BetRepository;
import com.example.cricket_app.repository.MatchRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.repository.WalletRepository;
import com.example.cricket_app.security.AuthUtils;
import com.example.cricket_app.service.BetService;
import com.example.cricket_app.service.WalletTransactionService;
import com.example.cricket_app.thread.AsyncBet;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BetServiceImpl implements BetService {
    private final Map<Long, Object> userLocks = new ConcurrentHashMap<>();
    private UserRepository userRepository;
    private MatchRepository matchRepository;
    private BetRepository betRepository;
    private WalletTransactionService walletTransactionService;
    private BetMapper betMapper;
    private AsyncBet asyncBet;
    private WalletRepository walletRepository;

    @Autowired
    public BetServiceImpl(UserRepository userRepository, MatchRepository matchRepository, BetRepository betRepository, WalletTransactionService walletTransactionService, BetMapper betMapper, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
        this.walletTransactionService = walletTransactionService;
        this.betMapper = betMapper;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public BetResponse placeBet(BetRequest request) {
        Long userId = AuthUtils.getLoggedInUserId();
//        Object lock = userLocks.computeIfAbsent(userId, k -> new Object());
//        asyncBet.processBetData(userId);
//        synchronized (lock) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        String teamChosen = String.valueOf(request.getTeamChosen());
        String teamA = match.getTeamA();
        String teamB = match.getTeamB();
        if (!teamChosen.equalsIgnoreCase(teamA) && !teamChosen.equalsIgnoreCase(teamB)) {
            throw new InvalidTeamChosenException("Chosen team is not participating in this match.");
        }

        if (betRepository.existsByUserAndMatch(user, match)) {
            throw new DuplicateBetException("User already placed a bet for this match.");
        }

        if (match.getStatus() == MatchStatus.ONGOING || match.getStatus() == MatchStatus.COMPLETED) {
            throw new OngoingMatchException("Bets are not allowed after the match has started.");
        }

        BigDecimal betAmount = match.getBetAmount();

        try {
            System.out.println("Sleeping 10s for user: " + userId + " on match: " + match.getId());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }//just trying to keep delay.to test.

        walletTransactionService.debitFromWallet(user.getId(), betAmount,
                "Bet placed on match " + match.getId(), match.getId());

        walletRepository.flush();

        Bet bet = new Bet();
        bet.setUser(user);//passing the whole object because we cant only pass the user_id.
        bet.setMatch(match);
        bet.setTeamChosen(request.getTeamChosen());
        bet.setAmount(betAmount);
        bet.setStatus(BetStatus.PENDING);
        bet.setCreatedAt(LocalDateTime.now());
        betRepository.save(bet);

        return betMapper.toResponse(bet);
//    }
    }


    @Override
    public void updateBetStatusesForMatchWinner(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        if (match.getWinningTeam() == null) {
            throw new MatchWinnerNotDeclaredException("Match winner is not declared yet.");
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
    public PagedBetResponse getUserBetHistory(int page, int size, String sortBy, String direction) {
        int pageNumber = Math.max(0, page - 1);
        int pageSize = Math.max(1, size);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        Long userId = AuthUtils.getLoggedInUserId();
        Page<Bet> bets = betRepository.findByUser_IdOrderByIdDesc(userId,pageable);
        List<BetResponse> betResponseList= bets.map(betMapper::toResponse).getContent();
        //betMapper is an object and toResponse refers to the instance method
        return new PagedBetResponse(
                betResponseList,
                bets.getNumber()+1,
                bets.getTotalPages(),
                bets.getTotalElements()
        );
    }
}
