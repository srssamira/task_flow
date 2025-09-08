package com.task_flow.task_flow.controllers;

import com.task_flow.task_flow.dtos.tasks.TaskCreateDTO;
import com.task_flow.task_flow.dtos.tasks.TaskResponseDTO;
import com.task_flow.task_flow.services.impl.TaskScheduling;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    TaskScheduling taskScheduling;

    public TaskController(TaskScheduling taskScheduling) {
        this.taskScheduling = taskScheduling;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO taskCreate) {
        TaskResponseDTO taskResponse = taskScheduling.createTask(taskCreate);
        return ResponseEntity.status(201).body(taskResponse);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks() {
        List<TaskResponseDTO> tasks = taskScheduling.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO taskResponse = taskScheduling.getTaskById(id);
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskCreateDTO taskUpdate) {
        TaskResponseDTO taskResponse = taskScheduling.updateTask(id, taskUpdate);
        return ResponseEntity.ok(taskResponse);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> markTaskAsCompleted(@PathVariable Long id) {
        TaskResponseDTO taskResponse = taskScheduling.markTaskAsCompleted(id);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskScheduling.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
