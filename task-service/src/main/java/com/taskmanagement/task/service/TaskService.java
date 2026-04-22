package com.taskmanagement.task.service;

import com.taskmanagement.task.dto.CreateTaskRequestDto;
import com.taskmanagement.task.dto.TaskResponseDto;
import com.taskmanagement.task.dto.UpdateTaskRequestDto;
import com.taskmanagement.task.dto.UpdateTaskStatusRequestDto;
import com.taskmanagement.task.entity.Project;
import com.taskmanagement.task.entity.Task;
import com.taskmanagement.task.repository.ProjectRepository;
import com.taskmanagement.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserResolver currentUserResolver;

    public TaskResponseDto createTask(CreateTaskRequestDto request, String username) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Long createdBy = currentUserResolver.resolveUserId(username);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .assigneeId(request.getAssigneeId())
                .createdBy(createdBy)
                .project(project)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Task savedTask = taskRepository.save(task);

        return mapToResponse(savedTask);
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return mapToResponse(task);
    }

    public TaskResponseDto updateTask(Long id, UpdateTaskRequestDto request, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Long currentUserId = currentUserResolver.resolveUserId(username);

        if (!task.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to update this task");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAssigneeId(request.getAssigneeId());
        task.setProject(project);
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);

        return mapToResponse(updatedTask);
    }

    public TaskResponseDto updateTaskStatus(Long id, UpdateTaskStatusRequestDto request, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Long currentUserId = currentUserResolver.resolveUserId(username);

        if (!task.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to update task status");
        }

        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);

        return mapToResponse(updatedTask);
    }

    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Long currentUserId = currentUserResolver.resolveUserId(username);

        if (!task.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to delete this task");
        }

        taskRepository.delete(task);
    }

    private TaskResponseDto mapToResponse(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assigneeId(task.getAssigneeId())
                .createdBy(task.getCreatedBy())
                .projectId(task.getProject().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}