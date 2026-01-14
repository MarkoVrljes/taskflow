package com.taskflow.taskflow.comment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByTaskIdOrderByCreatedAtAsc(UUID taskId);
}

