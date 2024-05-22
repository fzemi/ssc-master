package com.fzemi.sscmaster.task.controller;

import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    // TODO: Add mapper

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<Task>> getDeletedTasks() {
        return ResponseEntity.ok(taskService.getAllDeletedTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody Task task
    ) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable String id,
            @Valid @RequestBody Task task
    ) {
        task.setID(id);
        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
