package com.taskmanagement.task.controller;

import com.taskmanagement.task.dto.CommentResponseDto;
import com.taskmanagement.task.dto.CreateCommentRequestDto;
import com.taskmanagement.task.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponseDto createComment(@Valid @RequestBody CreateCommentRequestDto request,
                                            Authentication authentication) {
        return commentService.createComment(request, authentication.getName());
    }

    @GetMapping("/by-task/{taskId}")
    public List<CommentResponseDto> getByTask(@PathVariable Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }
}