package com.example.cricket_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateWalletRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}
