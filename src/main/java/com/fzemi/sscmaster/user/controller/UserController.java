package com.fzemi.sscmaster.user.controller;

import com.fzemi.sscmaster.user.entity.User;
import com.fzemi.sscmaster.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    // TODO: Add mapper

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<User>> getDeletedUsers() {
        return ResponseEntity.ok(userService.getAllDeletedUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @Valid @RequestBody User user
    ) {
        user.setID(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
