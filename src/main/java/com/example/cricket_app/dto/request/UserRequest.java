package com.example.cricket_app.dto.request;

import com.example.cricket_app.entity.Users;
import com.example.cricket_app.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String passwordHash;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private UserRole role = UserRole.PLAYER;


}
