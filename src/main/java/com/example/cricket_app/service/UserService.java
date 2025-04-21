package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.LoginRequest;
import com.example.cricket_app.dto.request.SignUpRequest;
import com.example.cricket_app.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;

public interface UserService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    SignUpResponse registerUser(SignUpRequest signUpRequest);

    SignUpResponse registerAdmin(SignUpRequest signUpRequest);

     PagedUserResponse showUsers(int page, int size, String sortBy, String direction);

     CompleteUserResponse getUserById(Long id,Pageable pageable);


}
