package com.task_flow.task_flow.repositories;

import com.task_flow.task_flow.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByCreatedAtGreaterThanEqual(LocalDateTime createdAt);
}
