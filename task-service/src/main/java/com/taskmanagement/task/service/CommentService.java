package com.taskmanagement.task.service;

import com.taskmanagement.task.dto.CommentResponseDto;
import com.taskmanagement.task.dto.CreateCommentRequestDto;
import com.taskmanagement.task.entity.Comment;
import com.taskmanagement.task.entity.Task;
import com.taskmanagement.task.repository.CommentRepository;
import com.taskmanagement.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentResponseDto createComment(CreateCommentRequestDto request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .task(task)
                .createdAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);

        return mapToResponse(saved);
    }

    public List<CommentResponseDto> getCommentsByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CommentResponseDto mapToResponse(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthorId())
                .taskId(comment.getTask().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}