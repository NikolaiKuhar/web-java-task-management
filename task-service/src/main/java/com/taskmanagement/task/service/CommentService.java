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
    private final CurrentUserResolver currentUserResolver;

    public CommentResponseDto createComment(CreateCommentRequestDto request, String username) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Long authorId = currentUserResolver.resolveUserId(username);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .authorId(authorId)
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

    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return mapToResponse(comment);
    }

    public void deleteComment(Long id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Long currentUserId = currentUserResolver.resolveUserId(username);

        if (!comment.getAuthorId().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
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