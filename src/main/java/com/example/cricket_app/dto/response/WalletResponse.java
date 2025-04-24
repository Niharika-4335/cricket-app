package com.example.cricket_app.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponse {

    private Long userId;
    private BigDecimal balance;
}
