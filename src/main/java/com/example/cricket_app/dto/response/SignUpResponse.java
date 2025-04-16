package com.example.cricket_app.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignUpResponse {
    private String fullName;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
