package com.taskmanagement.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/auth/hello")
    public String hello() {
        return "Auth service is running";
    }
}