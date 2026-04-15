package com.taskmanagement.auth.controller;

import com.taskmanagement.auth.dto.AuthResponseDto;
import com.taskmanagement.auth.dto.LoginRequestDto;
import com.taskmanagement.auth.dto.LoginResponseDto;
import com.taskmanagement.auth.dto.RegisterRequestDto;
import com.taskmanagement.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
}