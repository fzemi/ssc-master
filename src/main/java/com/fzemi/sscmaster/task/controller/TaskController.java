package com.fzemi.sscmaster.task.controller;

import com.fzemi.sscmaster.common.Mapper;
import com.fzemi.sscmaster.task.dto.TaskRequest;
import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final Mapper<Task, TaskRequest> taskMapper;

    public TaskController(
            TaskService taskService,
            Mapper<Task, TaskRequest> taskMapper
    ) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
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
            @Valid @RequestBody TaskRequest taskRequests
    ) {
        Task task = taskMapper.mapFromDTO(taskRequests);
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable String id,
            @Valid @RequestBody TaskRequest taskRequest
    ) {
        Task task = taskMapper.mapFromDTO(taskRequest);
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
