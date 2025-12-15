package com.floqdrive.service;

import com.floqdrive.entity.User;
import com.floqdrive.exception.NotFoundException;
import com.floqdrive.repository.UserRepository;

import com.floqdrive.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
{
    private final UserRepository userRepository;

    // Load user by ID from database
    public UserDetails loadUserById(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ot found"));

        return new UserDetailsImpl(user);
    }
}
