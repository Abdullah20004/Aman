package com.example.fintech_api.service;

import com.example.fintech_api.model.User;
import com.example.fintech_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsersByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }
}