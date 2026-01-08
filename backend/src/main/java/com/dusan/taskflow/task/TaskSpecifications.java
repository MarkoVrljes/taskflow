package com.dusan.taskflow.task;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public final class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<Task> forWorkspace(UUID workspaceId) {
        return (root, query, cb) -> cb.equal(root.get("workspaceId"), workspaceId);
    }

    public static Specification<Task> withStatus(TaskStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Task> withPriority(TaskPriority priority) {
        if (priority == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> withAssignee(UUID assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("assigneeId"), assigneeId);
    }

    public static Specification<Task> withSearch(String q) {
        if (q == null || q.isBlank()) {
            return null;
        }
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            Predicate title = cb.like(cb.lower(root.get("title")), like);
            Predicate description = cb.like(cb.lower(root.get("description")), like);
            return cb.or(title, description);
        };
    }
}
