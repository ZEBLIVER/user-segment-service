package com.example.user_segment_service.controller;

import com.example.user_segment_service.service.UserSegmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Пользователи и сегменты", description = "API для управления связями между пользователями и сегментами")
@RestController
public class UserSegmentController {

    private final UserSegmentService userSegmentService;

    public UserSegmentController(UserSegmentService userSegmentService) {
        this.userSegmentService = userSegmentService;
    }
    @Operation(summary = "Добавить пользователя в сегмент")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/addUserToSegment/{userId}")
    public ResponseEntity<String> addUserToSegment(@PathVariable Long userId,@RequestBody String segmentName) {
        try {
            userSegmentService.addUserToSegment(userId, segmentName);
            return ResponseEntity.ok("User added to segment successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user to segment: " + e.getMessage());
        }
    }

    @Operation(summary = "Удалить пользователя из сегмента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален из сегмента"),
            @ApiResponse(responseCode = "400", description = "Пользователь или сегмент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/removeUserFromSegment/{userId}")
    public ResponseEntity<String> removeUserFromSegment(@PathVariable Long userId,@RequestBody String segmentName) {
        try {
            userSegmentService.removeUserFromSegment(userId, segmentName);
            return ResponseEntity.ok("User remove from segment successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing user from segment: " + e.getMessage());
        }
    }

    @Operation(summary = "Распределить сегмент среди пользователей",
            description = "Случайным образом добавляет указанный процент пользователей в сегмент. " +
                    "Алгоритм учитывает пользователей, которые уже находятся в сегменте, и добавляет " +
                    "только необходимое количество новых пользователей для достижения указанного процента " +
                    "от общего числа пользователей в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сегмент успешно распределен"),
            @ApiResponse(responseCode = "400", description = "Сегмент не найден или процент некорректен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/distributeSegment")
    public ResponseEntity<String> distributeSegmentToPercentage(@RequestBody String segmentName,
                                                                @RequestParam double percentage) {
        try {
            userSegmentService.distributeSegmentToPercentage(segmentName,percentage);
            return ResponseEntity.ok("Segment distributed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error distributing segment: " + e.getMessage());
        }
    }

    @Operation(summary = "Получить сегменты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список сегментов успешно получен"),
            @ApiResponse(responseCode = "400", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/userSegments/{userId}")
    public ResponseEntity<List<String>> getUserSegments(@PathVariable Long userId) {
        try {
            List<String> segmentNames = userSegmentService.getUserSegments(userId);
            return ResponseEntity.ok(segmentNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}
