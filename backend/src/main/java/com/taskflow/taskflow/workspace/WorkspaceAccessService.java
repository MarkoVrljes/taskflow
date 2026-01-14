package com.taskflow.taskflow.workspace;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WorkspaceAccessService {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceAccessService(WorkspaceMemberRepository workspaceMemberRepository) {
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    public WorkspaceRole requireRole(UUID workspaceId, UUID userId) {
        return workspaceMemberRepository.findByIdWorkspaceIdAndIdUserId(workspaceId, userId)
                .map(WorkspaceMember::getRole)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));
    }

    public void requireMember(UUID workspaceId, UUID userId) {
        requireRole(workspaceId, userId);
    }

    public void requireRoleIn(UUID workspaceId, UUID userId, WorkspaceRole... allowed) {
        WorkspaceRole role = requireRole(workspaceId, userId);
        Set<WorkspaceRole> allowedSet = new HashSet<>(Arrays.asList(allowed));
        if (!allowedSet.contains(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
    }
}

