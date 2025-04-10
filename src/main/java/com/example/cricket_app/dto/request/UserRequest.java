package com.example.cricket_app.dto.request;

import com.example.cricket_app.entity.Users;
import com.example.cricket_app.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String passwordHash;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private UserRole role = UserRole.PLAYER;


}
