package com.task_flow.task_flow.infrastructure.presentation.mappers;

import com.task_flow.task_flow.application.dtos.tasks.TaskCreateDTO;
import com.task_flow.task_flow.application.dtos.tasks.TaskResponseDTO;
import com.task_flow.task_flow.domain.entities.TaskEntity;

public class TaskMapper {

    public static TaskEntity createToEntity(TaskCreateDTO taskCreate) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskCreate.getTitle());
        taskEntity.setDescription(taskCreate.getDescription());
        taskEntity.setCompleted(taskCreate.isCompleted());
        taskEntity.setCreatedAt(taskCreate.getCreatedAt());
        taskEntity.setDueDate(taskCreate.getDueDate());
        taskEntity.setPriority(taskCreate.getPriority());
        taskEntity.setCategory(taskCreate.getCategory());
        taskEntity.setComments(taskCreate.getComments());
        taskEntity.setRecurrenceRule(taskCreate.getRecurrenceRule());
        taskEntity.setActualTime(taskCreate.getActualTime());
        taskEntity.setEstimatedTime(taskCreate.getEstimatedTime());
        taskEntity.setReminder(taskCreate.getReminder());
        return taskEntity;
    }

    public static TaskResponseDTO entityToResponse(TaskEntity taskEntity) {
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        taskResponse.setId(taskEntity.getId());
        taskResponse.setTitle(taskEntity.getTitle());
        taskResponse.setDescription(taskEntity.getDescription());
        taskResponse.setCompleted(taskEntity.isCompleted());
        taskResponse.setCreatedAt(taskEntity.getCreatedAt());
        taskResponse.setDueDate(taskEntity.getDueDate());
        taskResponse.setPriority(taskEntity.getPriority());
        taskResponse.setCategory(taskEntity.getCategory());
        taskResponse.setComments(taskEntity.getComments());
        taskResponse.setRecurrenceRule(taskEntity.getRecurrenceRule());
        taskResponse.setActualTime(taskEntity.getActualTime());
        taskResponse.setEstimatedTime(taskEntity.getEstimatedTime());
        taskResponse.setReminder(taskEntity.getReminder());
        return taskResponse;
    }
}
