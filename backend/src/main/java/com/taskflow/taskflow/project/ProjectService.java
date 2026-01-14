package com.taskflow.taskflow.project;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskflow.taskflow.auth.CurrentUserService;
import com.taskflow.taskflow.project.dto.ProjectCreateRequest;
import com.taskflow.taskflow.project.dto.ProjectResponse;
import com.taskflow.taskflow.workspace.WorkspaceAccessService;
import com.taskflow.taskflow.workspace.WorkspaceRole;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final CurrentUserService currentUserService;

    public ProjectService(
            ProjectRepository projectRepository,
            WorkspaceAccessService workspaceAccessService,
            CurrentUserService currentUserService) {
        this.projectRepository = projectRepository;
        this.workspaceAccessService = workspaceAccessService;
        this.currentUserService = currentUserService;
    }

    public ProjectResponse create(UUID workspaceId, ProjectCreateRequest request) {
        UUID userId = currentUserService.requireUserId();
        workspaceAccessService.requireRoleIn(workspaceId, userId, WorkspaceRole.OWNER, WorkspaceRole.ADMIN);

        Project project = new Project();
        project.setWorkspaceId(workspaceId);
        project.setName(request.name());
        project.setDescription(request.description());
        projectRepository.save(project);

        return toResponse(project);
    }

    public List<ProjectResponse> list(UUID workspaceId) {
        UUID userId = currentUserService.requireUserId();
        workspaceAccessService.requireMember(workspaceId, userId);

        return projectRepository.findAllByWorkspaceId(workspaceId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Project requireProjectInWorkspace(UUID projectId, UUID workspaceId) {
        return projectRepository.findByIdAndWorkspaceId(projectId, workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getWorkspaceId(), project.getName(), project.getDescription());
    }
}

