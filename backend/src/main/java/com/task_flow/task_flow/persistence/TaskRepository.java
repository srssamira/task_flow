package com.task_flow.task_flow.persistence;

import com.task_flow.task_flow.domain.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByCreatedAtGreaterThanEqual(LocalDateTime createdAt);
}
