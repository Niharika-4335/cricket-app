package com.example.cricket_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_payout_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchPayoutStatus implements Serializable {
    @Id
    @Column(name = "match_id")
    private Long matchId; // Assuming Match uses Long for ID

    @OneToOne
    @MapsId // This maps the ID to the matchId
    @JoinColumn(name = "match_id")
    private Match match;//it means we are giving the same primary key and foreign key.
    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "total_pool_amount", precision = 10, scale = 2)
    private BigDecimal totalPoolAmount;

    @Column(name = "winners_count")
    private Integer winnersCount;

    @Column(name = "payout_per_winner", precision = 10, scale = 2)
    private BigDecimal payoutPerWinner;
}
