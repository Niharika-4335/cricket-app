package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.LoginRequest;
import com.example.cricket_app.dto.request.SignUpRequest;
import com.example.cricket_app.dto.response.JwtResponse;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    ResponseEntity<String> registerUser(SignUpRequest signUpRequest);

    ResponseEntity<String> registerAdmin(SignUpRequest signUpRequest);

    public List<UserResponse> showUsers();

    public UserResponse getUserById(Long id);


}
