package com.dusan.taskflow.workspace;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {
    boolean existsByIdWorkspaceIdAndIdUserId(UUID workspaceId, UUID userId);

    List<WorkspaceMember> findAllByIdUserId(UUID userId);

    java.util.Optional<WorkspaceMember> findByIdWorkspaceIdAndIdUserId(UUID workspaceId, UUID userId);
}
