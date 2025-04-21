package com.example.cricket_app.controller;

import com.example.cricket_app.dto.response.CompleteUserResponse;
import com.example.cricket_app.dto.response.PagedUserResponse;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public PagedUserResponse getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        return userService.showUsers(page, size, sortBy, direction);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public CompleteUserResponse getUserById(@PathVariable Long id,Pageable pageable) {
        return userService.getUserById(id,pageable);
    }

}
