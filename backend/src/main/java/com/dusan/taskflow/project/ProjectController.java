package com.dusan.taskflow.project;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dusan.taskflow.project.dto.ProjectCreateRequest;
import com.dusan.taskflow.project.dto.ProjectResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/workspaces/{workspaceId}/projects")
@Validated
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponse create(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody ProjectCreateRequest request) {
        return projectService.create(workspaceId, request);
    }

    @GetMapping
    public List<ProjectResponse> list(@PathVariable UUID workspaceId) {
        return projectService.list(workspaceId);
    }
}
