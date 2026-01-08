package com.dusan.taskflow.project;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dusan.taskflow.auth.CurrentUserService;
import com.dusan.taskflow.project.dto.ProjectCreateRequest;
import com.dusan.taskflow.project.dto.ProjectResponse;
import com.dusan.taskflow.workspace.WorkspaceMemberRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final CurrentUserService currentUserService;

    public ProjectService(
            ProjectRepository projectRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            CurrentUserService currentUserService) {
        this.projectRepository = projectRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.currentUserService = currentUserService;
    }

    public ProjectResponse create(UUID workspaceId, ProjectCreateRequest request) {
        UUID userId = currentUserService.requireUserId();
        if (!workspaceMemberRepository.existsByIdWorkspaceIdAndIdUserId(workspaceId, userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found");
        }

        Project project = new Project();
        project.setWorkspaceId(workspaceId);
        project.setName(request.name());
        project.setDescription(request.description());
        projectRepository.save(project);

        return toResponse(project);
    }

    public List<ProjectResponse> list(UUID workspaceId) {
        UUID userId = currentUserService.requireUserId();
        if (!workspaceMemberRepository.existsByIdWorkspaceIdAndIdUserId(workspaceId, userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found");
        }

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
