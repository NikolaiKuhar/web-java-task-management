package com.taskmanagement.task.service;

import com.taskmanagement.task.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthUserClient {

    private final RestTemplate restTemplate;

    public UserResponseDto getUserByUsername(String username) {
        String url = "http://localhost:8081/auth/users/by-username/" + username;

        UserResponseDto user = restTemplate.getForObject(url, UserResponseDto.class);

        if (user == null) {
            throw new RuntimeException("User not found in auth-service");
        }

        return user;
    }
}