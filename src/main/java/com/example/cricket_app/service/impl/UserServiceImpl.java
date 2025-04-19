package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.CreateWalletRequest;
import com.example.cricket_app.dto.request.LoginRequest;
import com.example.cricket_app.dto.request.SignUpRequest;
import com.example.cricket_app.dto.response.CompleteUserResponse;
import com.example.cricket_app.dto.response.JwtResponse;
import com.example.cricket_app.dto.response.SignUpResponse;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Bet;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.Wallet;
import com.example.cricket_app.entity.WalletTransaction;
import com.example.cricket_app.enums.UserRole;
import com.example.cricket_app.exception.DuplicateEmailException;
import com.example.cricket_app.exception.UserNotFoundException;
import com.example.cricket_app.mapper.BetMapper;
import com.example.cricket_app.mapper.SignUpMapper;
import com.example.cricket_app.mapper.UserMapper;
import com.example.cricket_app.mapper.WalletTransactionMapper;
import com.example.cricket_app.repository.BetRepository;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.repository.WalletTransactionRepository;
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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private SignUpMapper signUpMapper;
    private WalletService walletService;
    private WalletTransactionRepository walletTransactionRepository;
    private BetRepository betRepository;
    private WalletTransactionMapper walletTransactionMapper;
    private BetMapper betMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, SignUpMapper signUpMapper, WalletService walletService, WalletTransactionRepository walletTransactionRepository, BetRepository betRepository, WalletTransactionMapper walletTransactionMapper, BetMapper betMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.signUpMapper = signUpMapper;
        this.walletService = walletService;
        this.walletTransactionRepository = walletTransactionRepository;
        this.betRepository = betRepository;
        this.walletTransactionMapper = walletTransactionMapper;
        this.betMapper = betMapper;
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
        List<Users> users = userRepository.findByRole(UserRole.PLAYER);
        return userMapper.toResponseDtoList(users);
    }

    @Override
    public CompleteUserResponse getUserById(Long id) {
//        Users user = userRepository.findById(id).filter(i->i.getRole()==UserRole.PLAYER).orElseThrow(() -> new UserNotFoundException("user not found"));
//        return userMapper.toResponseDto(user);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Wallet wallet = user.getWallet();

        // Fetch transactions
        List<WalletTransaction> transactions = walletTransactionRepository
                .findByWallet_User_IdOrderByCreatedAtDesc(id);

        // Fetch bets
        List<Bet> bets = betRepository.findByUser_IdOrderByIdDesc(id);

        // Map to DTO
        CompleteUserResponse response = new CompleteUserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setBalance(wallet.getBalance());
        response.setTransactions(walletTransactionMapper.toResponseDtoList(transactions));
        response.setBets(bets.stream().map(betMapper::toResponse).collect(Collectors.toList()));

        return response;
    }

}

