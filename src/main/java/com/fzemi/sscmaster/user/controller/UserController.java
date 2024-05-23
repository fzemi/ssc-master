package com.fzemi.sscmaster.user.controller;

import com.fzemi.sscmaster.common.Mapper;
import com.fzemi.sscmaster.user.dto.UserRequest;
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
    private final Mapper<User, UserRequest> userMapper;

    public UserController(
            UserService userService,
            Mapper<User, UserRequest> userMapper
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
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
            @Valid @RequestBody UserRequest userRequest
    ) {
        User user = userMapper.mapFromDTO(userRequest);
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRequest userRequest
    ) {
        User user = userMapper.mapFromDTO(userRequest);
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
