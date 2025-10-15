package com.study.segment_service.controller;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.User;
import com.study.segment_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name="User Management")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный формат")
    })
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    })
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @Operation(summary = "Получить все сегменты пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список сегментов успешно возвращен"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    @GetMapping("/{userId}/segments")
    public ResponseEntity<Set<String>> getAllSegmentsByUserID(@PathVariable Long userId) {
        try {
            Set<String> segments = userService.getAllSegmentsByUserID(userId);
            return ResponseEntity.ok(segments);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
