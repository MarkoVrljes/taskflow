package com.taskflow.taskflow.workspace;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskflow.taskflow.auth.CurrentUserService;
import com.taskflow.taskflow.workspace.dto.WorkspaceCreateRequest;
import com.taskflow.taskflow.workspace.dto.WorkspaceResponse;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final CurrentUserService currentUserService;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            CurrentUserService currentUserService) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.currentUserService = currentUserService;
    }

    public WorkspaceResponse createWorkspace(WorkspaceCreateRequest request) {
        UUID userId = currentUserService.requireUserId();

        Workspace workspace = new Workspace();
        workspace.setName(request.name());
        workspace.setCreatedBy(userId);
        workspaceRepository.save(workspace);

        WorkspaceMember member = new WorkspaceMember();
        member.setId(new WorkspaceMemberId(workspace.getId(), userId));
        member.setRole(WorkspaceRole.OWNER);
        workspaceMemberRepository.save(member);

        return new WorkspaceResponse(workspace.getId(), workspace.getName());
    }

    public List<WorkspaceResponse> listMyWorkspaces() {
        UUID userId = currentUserService.requireUserId();
        return workspaceMemberRepository.findAllByIdUserId(userId).stream()
                .map(member -> workspaceRepository.findById(member.getId().getWorkspaceId())
                        .orElse(null))
                .filter(workspace -> workspace != null)
                .map(workspace -> new WorkspaceResponse(workspace.getId(), workspace.getName()))
                .collect(Collectors.toList());
    }

    public WorkspaceResponse getWorkspace(UUID workspaceId) {
        UUID userId = currentUserService.requireUserId();
        if (!workspaceMemberRepository.existsByIdWorkspaceIdAndIdUserId(workspaceId, userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found");
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));
        return new WorkspaceResponse(workspace.getId(), workspace.getName());
    }
}

