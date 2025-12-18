package com.floqdrive.service.impl;


import com.floqdrive.dto.LoginRequest;
import com.floqdrive.dto.RegisterRequest;
import com.floqdrive.entity.User;
import com.floqdrive.exception.NotFoundException;
import com.floqdrive.repository.UserRepository;
import com.floqdrive.security.JwtProvider;
import com.floqdrive.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public void register(RegisterRequest registerRequest)
    {
        // Check username exists
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent())
        {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest loginRequest)
    {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        {
            throw new IllegalArgumentException("Invalid password");
        }

        // Return JWT
        return jwtProvider.generateToken(user);
    }
}
