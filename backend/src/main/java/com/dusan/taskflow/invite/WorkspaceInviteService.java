package com.dusan.taskflow.invite;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dusan.taskflow.auth.CurrentUserService;
import com.dusan.taskflow.invite.dto.InviteAcceptResponse;
import com.dusan.taskflow.invite.dto.InviteCreateRequest;
import com.dusan.taskflow.invite.dto.InviteResponse;
import com.dusan.taskflow.user.User;
import com.dusan.taskflow.user.UserRepository;
import com.dusan.taskflow.workspace.WorkspaceAccessService;
import com.dusan.taskflow.workspace.WorkspaceMember;
import com.dusan.taskflow.workspace.WorkspaceMemberId;
import com.dusan.taskflow.workspace.WorkspaceMemberRepository;
import com.dusan.taskflow.workspace.WorkspaceRole;

@Service
public class WorkspaceInviteService {

    private static final Duration INVITE_TTL = Duration.ofDays(7);

    private final WorkspaceInviteRepository inviteRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public WorkspaceInviteService(
            WorkspaceInviteRepository inviteRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            WorkspaceAccessService workspaceAccessService,
            CurrentUserService currentUserService,
            UserRepository userRepository) {
        this.inviteRepository = inviteRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceAccessService = workspaceAccessService;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    public InviteResponse createInvite(UUID workspaceId, InviteCreateRequest request) {
        UUID userId = currentUserService.requireUserId();
        workspaceAccessService.requireRoleIn(workspaceId, userId, WorkspaceRole.OWNER, WorkspaceRole.ADMIN);

        WorkspaceInvite invite = new WorkspaceInvite();
        invite.setWorkspaceId(workspaceId);
        invite.setEmail(normalizeEmail(request.email()));
        invite.setRole(request.role());
        invite.setToken(UUID.randomUUID().toString());
        invite.setExpiresAt(Instant.now().plus(INVITE_TTL));
        inviteRepository.save(invite);

        return toResponse(invite);
    }

    public InviteAcceptResponse acceptInvite(String token) {
        UUID userId = currentUserService.requireUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        WorkspaceInvite invite = inviteRepository
                .findByTokenAndAcceptedAtIsNullAndExpiresAtAfter(token, Instant.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite not found"));

        if (!normalizeEmail(user.getEmail()).equals(normalizeEmail(invite.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invite email mismatch");
        }

        WorkspaceMemberId memberId = new WorkspaceMemberId(invite.getWorkspaceId(), userId);
        if (workspaceMemberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already a member");
        }

        WorkspaceMember member = new WorkspaceMember();
        member.setId(memberId);
        member.setRole(invite.getRole());
        workspaceMemberRepository.save(member);

        invite.setAcceptedAt(Instant.now());
        inviteRepository.save(invite);

        return new InviteAcceptResponse(invite.getWorkspaceId(), invite.getRole());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private InviteResponse toResponse(WorkspaceInvite invite) {
        return new InviteResponse(
                invite.getId(),
                invite.getWorkspaceId(),
                invite.getEmail(),
                invite.getRole(),
                invite.getToken(),
                invite.getExpiresAt(),
                invite.getAcceptedAt());
    }
}
