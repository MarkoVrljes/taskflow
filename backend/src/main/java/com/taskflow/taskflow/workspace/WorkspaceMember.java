package com.taskflow.taskflow.workspace;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "workspace_members")
public class WorkspaceMember {

    @EmbeddedId
    private WorkspaceMemberId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkspaceRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public WorkspaceMemberId getId() {
        return id;
    }

    public void setId(WorkspaceMemberId id) {
        this.id = id;
    }

    public WorkspaceRole getRole() {
        return role;
    }

    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

