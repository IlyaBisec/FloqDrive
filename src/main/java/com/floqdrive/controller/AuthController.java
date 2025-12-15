package com.floqdrive.controller;

import com.floqdrive.dto.LoginRequest;
import com.floqdrive.dto.RegisterRequest;
import com.floqdrive.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        authService.register(registerRequest);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest)
    {
        return authService.login(loginRequest); // Return JWT
    }
}
