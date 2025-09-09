package com.task_flow.task_flow.domain.services;

import com.task_flow.task_flow.application.dtos.tasks.TaskCreateDTO;
import com.task_flow.task_flow.application.dtos.tasks.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskCreateDTO taskCreate);
    List<TaskResponseDTO> getTasks();
    TaskResponseDTO getTaskById(Long id);
    TaskResponseDTO updateTask(Long d, TaskCreateDTO taskUpdate);
    TaskResponseDTO markTaskAsCompleted(Long id);
    void deleteTask(Long id);
}
