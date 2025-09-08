package com.task_flow.task_flow.controllers;

import com.task_flow.task_flow.dtos.TaskCreateDTO;
import com.task_flow.task_flow.dtos.TaskResponseDTO;
import com.task_flow.task_flow.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO taskCreate) {
        TaskResponseDTO taskResponse = taskService.createTask(taskCreate);
        return ResponseEntity.status(201).body(taskResponse);
    }
}
