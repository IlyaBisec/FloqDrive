package com.floqdrive.controller;

import com.floqdrive.dto.LoginRequest;
import com.floqdrive.dto.RegisterRequest;
import com.floqdrive.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

// Registration and login
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    // Service where business logic registration and login located
    private final AuthService authService;

    // Registration new user
    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        authService.register(registerRequest);
    }

    // Login user
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest)
    {
        return authService.login(loginRequest); // Return JWT
    }
}
