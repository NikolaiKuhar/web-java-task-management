package com.taskmanagement.task.dto;

import com.taskmanagement.task.entity.TaskPriority;
import com.taskmanagement.task.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private Long createdBy;
    private Long projectId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}