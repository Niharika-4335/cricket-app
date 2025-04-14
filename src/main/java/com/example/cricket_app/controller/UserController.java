package com.example.cricket_app.controller;

import com.example.cricket_app.dto.request.UserRequest;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    @Transactional
    public void CreateUser(@Valid @RequestBody UserRequest userRequest) {
         userService.saveUser(userRequest);
    }

    @GetMapping()
    public List<UserResponse> getListOfUsers() {
        return userService.showUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }


}
