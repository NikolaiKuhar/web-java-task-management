package com.taskmanagement.task.repository;

import com.taskmanagement.task.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}