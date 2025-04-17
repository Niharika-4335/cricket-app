package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long userId);

    Optional<Wallet> findByUser(Users user);

    @Query("SELECT u FROM Users u WHERE u.role = 'ADMIN'")
    Optional<Users> findAdminUser();


}
