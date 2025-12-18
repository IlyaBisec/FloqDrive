package com.floqdrive.service;

import com.floqdrive.dto.LoginRequest;
import com.floqdrive.dto.RegisterRequest;

public interface AuthService
{
    // Registration new user, save to database
    void register(RegisterRequest registerRequest);
    // Login user and give JWT token
    String login(LoginRequest loginRequest);
}
