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
//        System.out.println(userRequestDto.getEmail());
//        System.out.println(userRequestDto.getPasswordHash());
//        System.out.println(userRequestDto.getFullName());
        Users user = userMapper.toEntity(userRequest);
        //we are taking dto and converting into entity using mapper.
        //before updating to database we will change to entity product
//        System.out.println(user.getEmail());
//        System.out.println(user.getPasswordHash());
//        System.out.println(user.getFullName());
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
