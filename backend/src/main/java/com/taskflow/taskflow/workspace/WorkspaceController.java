package com.taskflow.taskflow.workspace;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.taskflow.workspace.dto.WorkspaceCreateRequest;
import com.taskflow.taskflow.workspace.dto.WorkspaceResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/workspaces")
@Validated
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public WorkspaceResponse create(@Valid @RequestBody WorkspaceCreateRequest request) {
        return workspaceService.createWorkspace(request);
    }

    @GetMapping
    public List<WorkspaceResponse> listMine() {
        return workspaceService.listMyWorkspaces();
    }

    @GetMapping("/{id}")
    public WorkspaceResponse get(@PathVariable("id") UUID id) {
        return workspaceService.getWorkspace(id);
    }
}

