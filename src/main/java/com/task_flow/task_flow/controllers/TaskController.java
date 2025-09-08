package com.task_flow.task_flow.controllers;

import com.task_flow.task_flow.dtos.TaskCreateDTO;
import com.task_flow.task_flow.dtos.TaskResponseDTO;
import com.task_flow.task_flow.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks() {
        List<TaskResponseDTO> tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO taskResponse = taskService.getTaskById(id);
        return ResponseEntity.ok(taskResponse);
    }
}
