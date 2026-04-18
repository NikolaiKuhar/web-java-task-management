package com.taskmanagement.task.controller;

import com.taskmanagement.task.dto.CreateTaskRequestDto;
import com.taskmanagement.task.dto.TaskResponseDto;
import com.taskmanagement.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponseDto createTask(@Valid @RequestBody CreateTaskRequestDto request,
                                      Authentication authentication) {
        return taskService.createTask(request, authentication.getName());
    }

    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}