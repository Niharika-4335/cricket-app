package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayOutRepository extends JpaRepository<Payout, Long> {
}
