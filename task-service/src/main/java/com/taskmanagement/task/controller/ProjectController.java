package com.taskmanagement.task.controller;

import com.taskmanagement.task.dto.CreateProjectRequestDto;
import com.taskmanagement.task.dto.ProjectResponseDto;
import com.taskmanagement.task.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponseDto createProject(@Valid @RequestBody CreateProjectRequestDto request) {
        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectResponseDto> getAllProjects() {
        return projectService.getAllProjects();
    }
}