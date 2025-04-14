package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayOutRepository extends JpaRepository<Payout, Long> {
    Optional<Payout> findByMatch_Id(Long matchId);
    //here in payout entity match is there we are getting id.

}
