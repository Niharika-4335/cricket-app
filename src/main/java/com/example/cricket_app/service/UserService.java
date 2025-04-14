package com.example.cricket_app.service;

import com.example.cricket_app.dto.request.UserRequest;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;

import java.util.List;

public interface UserService {
    public void saveUser(UserRequest userRequest);

    public List<UserResponse> showUsers();

    public UserResponse findUserById(Long id);

}
