package com.task_flow.task_flow.domain.services.impl;

import com.task_flow.task_flow.application.dtos.tasks.TaskCreateDTO;
import com.task_flow.task_flow.application.dtos.tasks.TaskResponseDTO;
import com.task_flow.task_flow.domain.entities.TaskEntity;
import com.task_flow.task_flow.persistence.TaskRepository;
import com.task_flow.task_flow.domain.services.TaskService;
import com.task_flow.task_flow.infrastructure.presentation.mappers.TaskMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskCreateDTO taskCreate) {
        TaskEntity taskEntity = TaskMapper.createToEntity(taskCreate);
        taskEntity = taskRepository.save(taskEntity);
        return TaskMapper.entityToResponse((taskEntity));
    }

    @Override
    @Transactional
    public List<TaskResponseDTO> getTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return taskEntities.stream()
                .map(TaskMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDTO getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return TaskMapper.entityToResponse(taskEntity);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public TaskResponseDTO markTaskAsCompleted(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskEntity.setCompleted(true);
        taskEntity = taskRepository.save(taskEntity);
        return TaskMapper.entityToResponse(taskEntity);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(taskEntity);
    }
}
