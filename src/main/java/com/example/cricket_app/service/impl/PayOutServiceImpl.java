package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.response.PayOutSummaryResponse;
import com.example.cricket_app.dto.response.WinnerPayOutInfo;
import com.example.cricket_app.entity.*;
import com.example.cricket_app.enums.MatchStatus;
import com.example.cricket_app.enums.TransactionType;
import com.example.cricket_app.exception.AdminNotFoundException;
import com.example.cricket_app.exception.MatchNotCompletedException;
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

        if (match.getStatus() != MatchStatus.COMPLETED) {
            throw new MatchNotCompletedException("Payoutsummary cannot be processed: Match is not completed yet.");
        }

        List<Payout> payouts = payOutRepository.findAllByMatch_Id(match.getId());
        if (!payouts.isEmpty()) {
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
                .toList();

        List<Bet> losingBets = allBets.stream()
                .filter(b -> !b.getTeamChosen().equals(match.getWinningTeam()))
                .toList();

        //if all are losers.admin takes full money.
        if (winningBets.isEmpty()) {
            BigDecimal totalLosingPool = BigDecimal.ZERO;
            for (Bet bet : losingBets) {
                totalLosingPool = totalLosingPool.add(bet.getAmount());
            }

            Users admin = walletRepository.findAdminUser()
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            Wallet adminWallet = walletRepository.findByUser(admin)
                    .orElseThrow(() -> new WalletNotFoundException("Admin wallet not found"));

            adminWallet.setBalance(adminWallet.getBalance().add(totalLosingPool));
            walletRepository.save(adminWallet);

            WalletTransaction adminTxn = new WalletTransaction();
            adminTxn.setWallet(adminWallet);
            adminTxn.setAmount(totalLosingPool);
            adminTxn.setTransactionType(TransactionType.ADMIN_COMMISSION);
            adminTxn.setDescription("Full losing pool taken by admin (no winners) for match " + match.getId());
            adminTxn.setMatch(match);
            walletTransactionRepository.save(adminTxn);

            return new PayOutSummaryResponse(match.getId(), totalLosingPool, BigDecimal.ZERO, List.of());
        }

        // if all are winners.now wins.
        if (losingBets.isEmpty()) {

            List<WinnerPayOutInfo> winners = winningBets.stream()
                    .map(bet -> new WinnerPayOutInfo(
                            bet.getUser().getId(),
                            bet.getUser().getFullName(),
                            BigDecimal.ZERO // No payout, just return winners with 0
                    ))
                    .toList();
            return new PayOutSummaryResponse(match.getId(), BigDecimal.ZERO, BigDecimal.ZERO, winners);
        }

        //  Admin-20% won players-80%.
        BigDecimal totalLosingPool = BigDecimal.ZERO;
        for (Bet bet : losingBets) {
            totalLosingPool = totalLosingPool.add(bet.getAmount());
        }


        BigDecimal adminCut = totalLosingPool.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.DOWN);
        BigDecimal distributableAmount = totalLosingPool.subtract(adminCut);

        // crediting to admin
        Users admin = walletRepository.findAdminUser()
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));
        Wallet adminWallet = walletRepository.findByUser(admin)
                .orElseThrow(() -> new WalletNotFoundException("Admin wallet not found"));

        adminWallet.setBalance(adminWallet.getBalance().add(adminCut));
        walletRepository.save(adminWallet);

        WalletTransaction adminTxn = new WalletTransaction();
        adminTxn.setWallet(adminWallet);
        adminTxn.setAmount(adminCut);
        adminTxn.setTransactionType(TransactionType.ADMIN_COMMISSION);
        adminTxn.setDescription("5% commission for match " + match.getId());
        adminTxn.setMatch(match);
        walletTransactionRepository.save(adminTxn);

        int numberOfWinners = winningBets.size();
        BigDecimal payoutPerWinner = distributableAmount.divide(
                new BigDecimal(numberOfWinners), 2, RoundingMode.DOWN
        );

        List<WinnerPayOutInfo> winnerInfos = new ArrayList<>();
        for (Bet bet : winningBets) {
            Users user = bet.getUser();
            Wallet wallet = walletRepository.findByUser(user)
                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

            wallet.setBalance(wallet.getBalance().add(payoutPerWinner).add(bet.getAmount()));
            walletRepository.save(wallet);

            WalletTransaction transaction = new WalletTransaction();
            transaction.setWallet(wallet);
            transaction.setAmount(payoutPerWinner.add(bet.getAmount()));
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

//    @Override
//    public PagedPayOutSummaryResponse getAllPayoutSummaries(int page, int size, String sortBy, String direction) {
//        int pageNumber = Math.max(0, page - 1);
//        int pageSize = Math.max(1, size);
//        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
//
//        // Fetch matches that are COMPLETED
//        Page<Match> completedMatches = matchRepository.findAllByStatus(MatchStatus.COMPLETED, pageable);
//
//        List<PayOutSummaryResponse> summaries = new ArrayList<>();
//
//        for (Match match : completedMatches.getContent()) {
//            try {
//                summaries.add(processPayout(match.getId()));
//            } catch (Exception ex) {
//            }
//        }
//
//        return new PagedPayOutSummaryResponse(
//                summaries,
//                completedMatches.getNumber()+ 1,
//                completedMatches.getTotalPages(),
//                completedMatches.getTotalElements()
//        );
//    }
}
