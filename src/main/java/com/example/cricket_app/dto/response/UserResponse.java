package com.example.cricket_app.dto.response;

import com.example.cricket_app.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
