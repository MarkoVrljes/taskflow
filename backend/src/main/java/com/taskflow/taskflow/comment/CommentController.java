package com.taskflow.taskflow.comment;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.taskflow.comment.dto.CommentCreateRequest;
import com.taskflow.taskflow.comment.dto.CommentResponse;

import jakarta.validation.Valid;

@RestController
@Validated
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/tasks/{taskId}/comments")
    public CommentResponse create(
            @PathVariable UUID taskId,
            @Valid @RequestBody CommentCreateRequest request) {
        return commentService.addComment(taskId, request);
    }

    @GetMapping("/tasks/{taskId}/comments")
    public List<CommentResponse> list(@PathVariable UUID taskId) {
        return commentService.listByTask(taskId);
    }
}

