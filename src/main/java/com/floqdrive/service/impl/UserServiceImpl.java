package com.floqdrive.service.impl;

import com.floqdrive.entity.User;
import com.floqdrive.exception.NotFoundException;
import com.floqdrive.repository.UserRepository;
import com.floqdrive.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
