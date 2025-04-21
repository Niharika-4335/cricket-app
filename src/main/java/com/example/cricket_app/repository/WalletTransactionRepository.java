package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Wallet;
import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    Page<WalletTransaction> findByWallet_User_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

//    List<WalletTransaction> findByMatch_IdAndTransactionType(Long id, TransactionType transactionType);

    //here also using nested queries.we get user_id by using wallet.
    //wallet contains user object.therefore wallet_user_id all must be in caps.







}
