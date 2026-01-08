package com.dusan.taskflow.task;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taskflow.task.dto.TaskCreateRequest;
import com.dusan.taskflow.task.dto.TaskResponse;
import com.dusan.taskflow.task.dto.TaskUpdateRequest;

import jakarta.validation.Valid;

@RestController
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/projects/{projectId}/tasks")
    public TaskResponse create(
            @PathVariable UUID projectId,
            @Valid @RequestBody TaskCreateRequest request) {
        return taskService.create(projectId, request);
    }

    @GetMapping("/workspaces/{workspaceId}/tasks")
    public Page<TaskResponse> list(
            @PathVariable UUID workspaceId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) UUID assigneeId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {
        return taskService.list(workspaceId, status, priority, assigneeId, q, page, size, sort);
    }

    @GetMapping("/tasks/{taskId}")
    public TaskResponse get(@PathVariable UUID taskId) {
        return taskService.get(taskId);
    }

    @PatchMapping("/tasks/{taskId}")
    public TaskResponse update(
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.update(taskId, request);
    }

    @DeleteMapping("/tasks/{taskId}")
    public void delete(@PathVariable UUID taskId) {
        taskService.delete(taskId);
    }
}
