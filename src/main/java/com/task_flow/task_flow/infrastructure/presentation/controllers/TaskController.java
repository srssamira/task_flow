package com.task_flow.task_flow.infrastructure.presentation.controllers;

import com.task_flow.task_flow.application.dtos.tasks.TaskCreateDTO;
import com.task_flow.task_flow.application.dtos.tasks.TaskResponseDTO;
import com.task_flow.task_flow.domain.services.TaskService;
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

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskCreateDTO taskUpdate) {
        TaskResponseDTO taskResponse = taskService.updateTask(id, taskUpdate);
        return ResponseEntity.ok(taskResponse);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> markTaskAsCompleted(@PathVariable Long id) {
        TaskResponseDTO taskResponse = taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
