package com.task_flow.task_flow.infrastructure.persistence;

import com.task_flow.task_flow.domain.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    // O Spring Data JPA vai criar os métodos como findAll(), findById(), save(), etc.
    // automaticamente para você. Você não precisa escrever nada aqui por enquanto.
}