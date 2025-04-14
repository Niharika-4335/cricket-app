package com.example.cricket_app.service.impl;

import com.example.cricket_app.entity.*;
import com.example.cricket_app.enums.TransactionType;
import com.example.cricket_app.exception.WalletNotFoundException;
import com.example.cricket_app.repository.BetRepository;
import com.example.cricket_app.repository.PayOutRepository;
import com.example.cricket_app.repository.WalletRepository;
import com.example.cricket_app.repository.WalletTransactionRepository;
import com.example.cricket_app.service.PayOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class PayOutServiceImpl implements PayOutService {
    private BetRepository betRepository;
    private WalletRepository walletRepository;
    private WalletTransactionRepository walletTransactionRepository;
    private PayOutRepository payOutRepository;

    @Autowired
    public PayOutServiceImpl(BetRepository betRepository, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository, PayOutRepository payOutRepository) {
        this.betRepository = betRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.payOutRepository = payOutRepository;
    }

    @Override

    public void processPayout(Match match) {

        Optional<Payout> existingPayout = payOutRepository.findByMatch_Id(match.getId());

        if (existingPayout.isPresent()) {
            throw new IllegalStateException("Payout already processed for this match.");
        }
        List<Bet> allBets = betRepository.findByMatch(match);

        List<Bet> winningBets = allBets.stream()
                .filter(b -> b.getTeamChosen().equals(match.getWinningTeam()))
                .toList();//getting all the bet records who opted winning team.

        List<Bet> losingBets = allBets.stream()
                .filter(b -> !b.getTeamChosen().equals(match.getWinningTeam()))
                .toList();//getting all the bet records who opted losing team.

        if (winningBets == null || winningBets.size() == 0) {
            System.out.println("No winning bets to process.");
            return;
        }

        BigDecimal totalLosingPool = BigDecimal.ZERO;//initializing with 0 for the pool amount.
        for (int i = 0; i < losingBets.size(); i++) {
            Bet bet = losingBets.get(i);
            totalLosingPool = totalLosingPool.add(bet.getAmount());//adding all the losers bet amount to the pool.
        }


        int numberOfWinners = winningBets.size();
        BigDecimal payoutPerWinner = totalLosingPool.divide(
                new BigDecimal(numberOfWinners), 2, RoundingMode.DOWN
        );//amount payout per winner.(2 decimal places,rounding off every number).

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
        }
    }
}
