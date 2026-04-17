package com.taskmanagement.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectRequestDto {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    private Long ownerId;
}