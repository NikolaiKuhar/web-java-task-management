package com.taskmanagement.task.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureTestController {

    @GetMapping("/secure/me")
    public String secureMe(Authentication authentication) {
        return "Authenticated as: " + authentication.getName();
    }
}