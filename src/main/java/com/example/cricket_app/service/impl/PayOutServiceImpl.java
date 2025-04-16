package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.response.PayOutSummaryResponse;
import com.example.cricket_app.dto.response.WinnerPayOutInfo;
import com.example.cricket_app.entity.*;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.enums.TransactionType;
import com.example.cricket_app.exception.MatchNotFoundException;
import com.example.cricket_app.exception.WalletNotFoundException;
import com.example.cricket_app.repository.*;
import com.example.cricket_app.service.PayOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PayOutServiceImpl implements PayOutService {
    private BetRepository betRepository;
    private WalletRepository walletRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private PayOutRepository payOutRepository;
    private MatchRepository matchRepository;

    @Autowired
    public PayOutServiceImpl(BetRepository betRepository, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository, PayOutRepository payOutRepository, MatchRepository matchRepository) {
        this.betRepository = betRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.payOutRepository = payOutRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public PayOutSummaryResponse processPayout(Long matchId) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found with ID: " + matchId));


        if (match.getStatus()!= MatchStatus.COMPLETED) {
            throw new IllegalStateException("Payoutsummary cannot be processed: Match is not completed yet.");
        }

        List<Payout> payouts = payOutRepository.findAllByMatch_Id(match.getId());
        if (!payouts.isEmpty()) {//if already payouts processed and then we retrieve summary.
            BigDecimal payoutPerUser = payouts.get(0).getAmount();

            BigDecimal totalLosingPool = BigDecimal.ZERO;
            for (Payout payout : payouts) {
                totalLosingPool = totalLosingPool.add(payout.getAmount());
            }

            List<WinnerPayOutInfo> winners = payouts.stream().map(p -> {
                Users user = p.getUser();
                return new WinnerPayOutInfo(user.getId(), user.getFullName(), payoutPerUser);
            }).toList();

            return new PayOutSummaryResponse(match.getId(), totalLosingPool, payoutPerUser, winners);
        }


        List<Bet> allBets = betRepository.findByMatch(match);

        List<Bet> winningBets = allBets.stream()
                .filter(b -> b.getTeamChosen().equals(match.getWinningTeam()))
                .toList();//getting all the bet records who opted winning team.

        List<Bet> losingBets = allBets.stream()
                .filter(b -> !b.getTeamChosen().equals(match.getWinningTeam()))
                .toList();//getting all the bet records who opted losing team.


        //all are losers
        if (winningBets == null || winningBets.size() == 0) {
            System.out.println("No winning bets to process.");
            return new PayOutSummaryResponse(match.getId(), BigDecimal.ZERO, BigDecimal.ZERO, List.of());
        }


        //all are winners.
        if (losingBets.isEmpty()) {
            for (Bet bet : winningBets) {
                Users user = bet.getUser();
                Wallet wallet = walletRepository.findByUser(user)
                        .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

                wallet.setBalance(wallet.getBalance().add(bet.getAmount()));
                walletRepository.save(wallet);
                //if all are winning teams.then we will  refund the bet amount.
                WalletTransaction transaction = new WalletTransaction();
                transaction.setWallet(wallet);
                transaction.setAmount(bet.getAmount());
                transaction.setTransactionType(TransactionType.BET_REFUND);
                transaction.setDescription("Refund due to no losers in match " + match.getId());
                transaction.setMatch(match);
                walletTransactionRepository.save(transaction);
            }
            List<WinnerPayOutInfo> refundInfos = winningBets.stream()
                    .map(b -> new WinnerPayOutInfo(
                            b.getUser().getId(),
                            b.getUser().getFullName(),
                            b.getAmount()))
                    .toList();

            return new PayOutSummaryResponse(match.getId(), BigDecimal.ZERO, BigDecimal.ZERO, refundInfos);
        }


        //normal-flow.
        BigDecimal totalLosingPool = BigDecimal.ZERO;//initializing with 0 for the pool amount.
        for (int i = 0; i < losingBets.size(); i++) {
            Bet bet = losingBets.get(i);
            totalLosingPool = totalLosingPool.add(bet.getAmount());//adding all the losers bet amount to the pool.
        }


        int numberOfWinners = winningBets.size();
        BigDecimal payoutPerWinner = totalLosingPool.divide(
                new BigDecimal(numberOfWinners), 2, RoundingMode.DOWN
        );//amount payout per winner.(2 decimal places,rounding off every number).

        List<WinnerPayOutInfo> winnerInfos = new ArrayList<>();
        for (Bet bet : winningBets) {
            Users user = bet.getUser();
            Wallet wallet = walletRepository.findByUser(user)
                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

            wallet.setBalance(wallet.getBalance().add(payoutPerWinner));
            walletRepository.save(wallet);

            WalletTransaction transaction = new WalletTransaction();
            transaction.setWallet(wallet);
            transaction.setAmount(payoutPerWinner);
            transaction.setTransactionType(TransactionType.WIN_CREDIT);
            transaction.setDescription("Payout for match " + match.getId());
            transaction.setMatch(match);
            walletTransactionRepository.save(transaction);

            Payout payout = new Payout();
            payout.setMatch(match);
            payout.setUser(user);
            payout.setAmount(payoutPerWinner);
            payOutRepository.save(payout);
            winnerInfos.add(new WinnerPayOutInfo(user.getId(), user.getFullName(), payoutPerWinner));
        }
        return new PayOutSummaryResponse(match.getId(), totalLosingPool, payoutPerWinner, winnerInfos);
    }
}
