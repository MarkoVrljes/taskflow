package com.dusan.taskflow.task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dusan.taskflow.auth.CurrentUserService;
import com.dusan.taskflow.project.Project;
import com.dusan.taskflow.project.ProjectRepository;
import com.dusan.taskflow.task.dto.TaskCreateRequest;
import com.dusan.taskflow.task.dto.TaskResponse;
import com.dusan.taskflow.task.dto.TaskUpdateRequest;
import com.dusan.taskflow.workspace.WorkspaceMemberRepository;

@Service
public class TaskService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final Set<String> ALLOWED_SORT_FIELDS = new HashSet<>(
            Arrays.asList("createdAt", "updatedAt", "dueDate", "priority", "status"));

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final CurrentUserService currentUserService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            CurrentUserService currentUserService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.currentUserService = currentUserService;
    }

    public TaskResponse create(UUID projectId, TaskCreateRequest request) {
        UUID userId = currentUserService.requireUserId();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        requireMember(project.getWorkspaceId(), userId);

        Task task = new Task();
        task.setWorkspaceId(project.getWorkspaceId());
        task.setProjectId(projectId);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status() == null ? TaskStatus.TODO : request.status());
        task.setPriority(request.priority() == null ? TaskPriority.MED : request.priority());
        task.setAssigneeId(request.assigneeId());
        task.setCreatedBy(userId);
        task.setDueDate(request.dueDate());

        taskRepository.save(task);
        return toResponse(task);
    }

    public Page<TaskResponse> list(
            UUID workspaceId,
            TaskStatus status,
            TaskPriority priority,
            UUID assigneeId,
            String q,
            int page,
            int size,
            String sort) {
        UUID userId = currentUserService.requireUserId();
        requireMember(workspaceId, userId);

        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), safeSize, parseSort(sort));

        Specification<Task> spec = Specification.where(TaskSpecifications.forWorkspace(workspaceId));
        if (status != null) {
            spec = spec.and(TaskSpecifications.withStatus(status));
        }
        if (priority != null) {
            spec = spec.and(TaskSpecifications.withPriority(priority));
        }
        if (assigneeId != null) {
            spec = spec.and(TaskSpecifications.withAssignee(assigneeId));
        }
        if (q != null && !q.isBlank()) {
            spec = spec.and(TaskSpecifications.withSearch(q));
        }

        return taskRepository.findAll(spec, pageRequest).map(this::toResponse);
    }

    public TaskResponse get(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        UUID userId = currentUserService.requireUserId();
        requireMember(task.getWorkspaceId(), userId);

        return toResponse(task);
    }

    public TaskResponse update(UUID taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        UUID userId = currentUserService.requireUserId();
        requireMember(task.getWorkspaceId(), userId);

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.assigneeId() != null) {
            task.setAssigneeId(request.assigneeId());
        }
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }

        taskRepository.save(task);
        return toResponse(task);
    }

    public void delete(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        UUID userId = currentUserService.requireUserId();
        requireMember(task.getWorkspaceId(), userId);

        taskRepository.delete(task);
    }

    private void requireMember(UUID workspaceId, UUID userId) {
        if (!workspaceMemberRepository.existsByIdWorkspaceIdAndIdUserId(workspaceId, userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found");
        }
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",", 2);
        String field = parts[0].trim();
        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        Sort.Direction direction = Sort.Direction.DESC;
        if (parts.length == 2 && "asc".equalsIgnoreCase(parts[1].trim())) {
            direction = Sort.Direction.ASC;
        }
        return Sort.by(direction, field);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getWorkspaceId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssigneeId(),
                task.getCreatedBy(),
                task.getDueDate(),
                task.getVersion(),
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
