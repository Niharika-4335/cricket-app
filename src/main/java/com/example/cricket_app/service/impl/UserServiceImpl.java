package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.CreateWalletRequest;
import com.example.cricket_app.dto.request.LoginRequest;
import com.example.cricket_app.dto.request.SignUpRequest;
import com.example.cricket_app.dto.response.JwtResponse;
import com.example.cricket_app.dto.response.SignUpResponse;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.enums.UserRole;
import com.example.cricket_app.exception.DuplicateEmailException;
import com.example.cricket_app.exception.UserNotFoundException;
import com.example.cricket_app.mapper.SignUpMapper;
import com.example.cricket_app.mapper.UserMapper;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.security.CustomUserDetails;
import com.example.cricket_app.security.JwtUtils;
import com.example.cricket_app.service.UserService;
import com.example.cricket_app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private UserMapper  userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final SignUpMapper signUpMapper;
    private final WalletService walletService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, SignUpMapper signUpMapper, WalletService walletService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.signUpMapper = signUpMapper;
        this.walletService = walletService;
        this.userMapper = userMapper;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //getPrincipal means the authenticated user

        String jwt = jwtUtils.generateToken(
                userDetails.getUsername(),
                userDetails.getRole().name(),
                userDetails.getId()
        );

        JwtResponse response = new JwtResponse();
        response.setToken(jwt);
        response.setRole(userDetails.getRole().name());

        return response;
    }

    @Override
    public SignUpResponse registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException("Email is already registered.");
        }

        Users user = new Users();
        user.setEmail(signUpRequest.getEmail());
        user.setFullName(signUpRequest.getFullName());
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.PLAYER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        walletService.initializeWallet(new CreateWalletRequest(user.getId()));
        return signUpMapper.toResponseDto(user);

    }

    @Override
    public SignUpResponse registerAdmin(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
           throw new DuplicateEmailException("Email is already registered.");
        }

        Users admin = new Users();
        admin.setEmail(signUpRequest.getEmail());
        admin.setFullName(signUpRequest.getFullName());
        admin.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        admin.setRole(UserRole.ADMIN);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        userRepository.save(admin);
        walletService.initializeWallet(new CreateWalletRequest(admin.getId()));
        return signUpMapper.toResponseDto(admin);
    }

    @Override
    public List<UserResponse> showUsers() {
        List<Users> users=userRepository.findByRole(UserRole.PLAYER);
        return userMapper.toResponseDtoList(users);
    }

    @Override
    public UserResponse getUserById(Long id) {
        Users user = userRepository.findById(id).filter(i->i.getRole()==UserRole.PLAYER).orElseThrow(() -> new UserNotFoundException("user not found"));
        return userMapper.toResponseDto(user);
    }

    }

