package com.example.cricket_app.repository;

import com.example.cricket_app.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m WHERE m.status = 'UPCOMING' AND m.startTime > :now ORDER BY m.startTime ASC")
    List<Match> findUpcomingMatches(LocalDateTime now);

    @Query("SELECT m FROM Match m WHERE m.status = 'COMPLETED' ORDER BY m.startTime DESC")
    List<Match> findCompletedMatches();


}
