package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.LoginRequest;
import com.example.cricket_app.dto.request.SignUpRequest;
import com.example.cricket_app.dto.response.JwtResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.enums.UserRole;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.security.CustomUserDetails;
import com.example.cricket_app.security.JwtUtils;
import com.example.cricket_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {
    private final UserService userService;

    @Autowired
    public UserAuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user-signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @PostMapping("/admin-signup")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.registerAdmin(signUpRequest);
    }




}
