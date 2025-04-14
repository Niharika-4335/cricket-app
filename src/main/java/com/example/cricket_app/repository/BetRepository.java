package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Bet;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    boolean existsByUserAndMatch(Users user, Match match);

    List<Bet> findByUser_IdOrderByIdDesc(Long userId);

    List<Bet> findByMatch(Match match);

}
