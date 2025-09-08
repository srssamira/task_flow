package com.task_flow.task_flow.services;

import com.task_flow.task_flow.dtos.TaskCreateDTO;
import com.task_flow.task_flow.dtos.TaskResponseDTO;
import com.task_flow.task_flow.entities.TaskEntity;
import com.task_flow.task_flow.repositories.TaskRepository;
import com.task_flow.task_flow.services.mappers.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<TaskResponseDTO> getTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return taskEntities.stream()
                .map(TaskMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return TaskMapper.entityToResponse(taskEntity);
    }

    public TaskResponseDTO updateTask(Long id, TaskCreateDTO taskUpdate) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskEntity.setTitle(taskUpdate.getTitle());
        taskEntity.setDescription(taskUpdate.getDescription());
        taskEntity.setCompleted(taskUpdate.isCompleted());
        taskEntity.setDueDate(taskUpdate.getDueDate());
        taskEntity.setPriority(taskUpdate.getPriority());
        taskEntity.setCategory(taskUpdate.getCategory());
        taskEntity.setComments(taskUpdate.getComments());
        taskEntity.setRecurrenceRule(taskUpdate.getRecurrenceRule());
        taskEntity.setActualTime(taskUpdate.getActualTime());
        taskEntity.setEstimatedTime(taskUpdate.getEstimatedTime());
        taskEntity = taskRepository.save(taskEntity);
        return TaskMapper.entityToResponse(taskEntity);
    }

    public TaskResponseDTO markTaskAsCompleted(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskEntity.setCompleted(true);
        taskEntity = taskRepository.save(taskEntity);
        return TaskMapper.entityToResponse(taskEntity);
    }
}
