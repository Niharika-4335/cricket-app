package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.UserRequest;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;

import java.util.List;

public interface UserService {
    public UserResponse saveUser(UserRequest userRequest);

    public List<Users> showUsers();

    public Users findUserById(Long id);

}
