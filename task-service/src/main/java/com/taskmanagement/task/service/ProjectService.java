package com.taskmanagement.task.service;

import com.taskmanagement.task.dto.CreateProjectRequestDto;
import com.taskmanagement.task.dto.ProjectResponseDto;
import com.taskmanagement.task.dto.UpdateProjectRequestDto;
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
    private final CurrentUserResolver currentUserResolver;

    public ProjectResponseDto createProject(CreateProjectRequestDto request, String username) {
        Long ownerId = currentUserResolver.resolveUserId(username);

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(ownerId)
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

    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return mapToResponse(project);
    }

    public ProjectResponseDto updateProject(Long id, UpdateProjectRequestDto request, String username) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Long currentUserId = currentUserResolver.resolveUserId(username);

        if (!project.getOwnerId().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to update this project");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setUpdatedAt(LocalDateTime.now());

        Project updatedProject = projectRepository.save(project);

        return mapToResponse(updatedProject);
    }

    public void deleteProject(Long id, String username) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Long currentUserId = currentUserResolver.resolveUserId(username);

        if (!project.getOwnerId().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to delete this project");
        }

        projectRepository.delete(project);
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