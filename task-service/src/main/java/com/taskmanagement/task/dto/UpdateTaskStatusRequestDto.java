package com.taskmanagement.task.dto;

import com.taskmanagement.task.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusRequestDto {

    @NotNull(message = "Task status is required")
    private TaskStatus status;
}