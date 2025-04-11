package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Bet;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    boolean existsByUserAndMatch(Users user, Match match);
}
