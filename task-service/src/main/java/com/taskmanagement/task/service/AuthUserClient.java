package com.taskmanagement.task.service;

import com.taskmanagement.task.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthUserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public UserResponseDto getUserByUsername(String username) {
        String url = authServiceUrl + "/auth/users/by-username/" + username;
        return restTemplate.getForObject(url, UserResponseDto.class);
    }
}