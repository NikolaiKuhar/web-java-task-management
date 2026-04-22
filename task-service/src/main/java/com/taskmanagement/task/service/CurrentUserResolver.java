package com.taskmanagement.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserResolver {

    private final AuthUserClient authUserClient;

    public Long resolveUserId(String username) {
        return authUserClient.getUserByUsername(username).getId();
    }
}