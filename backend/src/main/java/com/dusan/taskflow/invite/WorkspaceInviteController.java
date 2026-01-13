package com.dusan.taskflow.invite;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taskflow.invite.dto.InviteAcceptResponse;
import com.dusan.taskflow.invite.dto.InviteCreateRequest;
import com.dusan.taskflow.invite.dto.InviteResponse;

import jakarta.validation.Valid;

@RestController
@Validated
public class WorkspaceInviteController {

    private final WorkspaceInviteService inviteService;

    public WorkspaceInviteController(WorkspaceInviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping("/workspaces/{workspaceId}/invites")
    public InviteResponse create(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody InviteCreateRequest request) {
        return inviteService.createInvite(workspaceId, request);
    }

    @PostMapping("/invites/accept")
    public InviteAcceptResponse accept(@RequestParam("token") String token) {
        return inviteService.acceptInvite(token);
    }
}
