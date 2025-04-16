package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.LoginRequest;
import com.example.cricket_app.dto.request.SignUpRequest;
import com.example.cricket_app.dto.response.JwtResponse;
import com.example.cricket_app.dto.response.SignUpResponse;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    SignUpResponse registerUser(SignUpRequest signUpRequest);

    SignUpResponse registerAdmin(SignUpRequest signUpRequest);

     List<UserResponse> showUsers();

     UserResponse getUserById(Long id);


}
