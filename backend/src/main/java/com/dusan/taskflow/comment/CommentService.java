package com.dusan.taskflow.comment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dusan.taskflow.auth.CurrentUserService;
import com.dusan.taskflow.comment.dto.CommentCreateRequest;
import com.dusan.taskflow.comment.dto.CommentResponse;
import com.dusan.taskflow.task.Task;
import com.dusan.taskflow.task.TaskRepository;
import com.dusan.taskflow.workspace.WorkspaceAccessService;
import com.dusan.taskflow.workspace.WorkspaceRole;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final CurrentUserService currentUserService;

    public CommentService(
            CommentRepository commentRepository,
            TaskRepository taskRepository,
            WorkspaceAccessService workspaceAccessService,
            CurrentUserService currentUserService) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.workspaceAccessService = workspaceAccessService;
        this.currentUserService = currentUserService;
    }

    public CommentResponse addComment(UUID taskId, CommentCreateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        UUID userId = currentUserService.requireUserId();
        workspaceAccessService.requireRoleIn(
                task.getWorkspaceId(),
                userId,
                WorkspaceRole.OWNER,
                WorkspaceRole.ADMIN,
                WorkspaceRole.MEMBER);

        Comment comment = new Comment();
        comment.setTaskId(task.getId());
        comment.setWorkspaceId(task.getWorkspaceId());
        comment.setAuthorId(userId);
        comment.setBody(request.body());
        commentRepository.save(comment);

        return toResponse(comment);
    }

    public List<CommentResponse> listByTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        UUID userId = currentUserService.requireUserId();
        workspaceAccessService.requireMember(task.getWorkspaceId(), userId);

        return commentRepository.findAllByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTaskId(),
                comment.getWorkspaceId(),
                comment.getAuthorId(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }
}
