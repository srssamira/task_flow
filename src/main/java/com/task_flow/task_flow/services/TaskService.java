package com.task_flow.task_flow.services;

import com.task_flow.task_flow.dtos.TaskCreateDTO;
import com.task_flow.task_flow.dtos.TaskResponseDTO;
import com.task_flow.task_flow.entities.TaskEntity;
import com.task_flow.task_flow.repositories.TaskRepository;
import com.task_flow.task_flow.services.mappers.TaskMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDTO createTask(TaskCreateDTO taskCreate) {
        TaskEntity taskEntity = TaskMapper.createToEntity(taskCreate);
        taskEntity = taskRepository.save(taskEntity);
        return TaskMapper.entityToResponse((taskEntity));
    }
}
