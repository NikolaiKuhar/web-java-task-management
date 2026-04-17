package com.taskmanagement.task.service;

import com.taskmanagement.task.dto.CreateProjectRequestDto;
import com.taskmanagement.task.dto.ProjectResponseDto;
import com.taskmanagement.task.entity.Project;
import com.taskmanagement.task.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponseDto createProject(CreateProjectRequestDto request) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(request.getOwnerId() != null ? request.getOwnerId() : 1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Project savedProject = projectRepository.save(project);

        return mapToResponse(savedProject);
    }

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProjectResponseDto mapToResponse(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwnerId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}