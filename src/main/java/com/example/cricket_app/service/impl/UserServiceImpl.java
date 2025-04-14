package com.example.cricket_app.service.impl;

import com.example.cricket_app.dto.request.UserRequest;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.mapper.UserMapper;
import com.example.cricket_app.repository.UserRepository;
import com.example.cricket_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        Users user = userMapper.toEntity(userRequest);
        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }


    @Override
    public List<Users> showUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users findUserById(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("not valid"));
        return user;

    }


}
