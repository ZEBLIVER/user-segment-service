package com.example.user_segment_service.controller;

import com.example.user_segment_service.model.Segment;
import com.example.user_segment_service.service.SegmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@Tag(name = "Сегменты", description = "API для управления сегментами пользователей")
@RestController
public class SegmentController {
    private final SegmentService segmentService;

    @Autowired
    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @Operation(summary = "Добавить новый сегмент", description = "Создает новый сегмент в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сегмент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Сегмент с таким названием уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/addSegment")
    public ResponseEntity<String> addSegment(@RequestBody Segment segment) {
        try {
            segmentService.addNewSegment(segment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Segment added successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    @Operation(summary = "Удалить сегмент", description = "Удаляет сегмент по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сегмент успешно удален"),
            @ApiResponse(responseCode = "400", description = "Сегмент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/delSegment/{id}")
    public ResponseEntity<String> delSegment(@PathVariable Long id) {
        try {
            segmentService.deleteSegment(id);
            return ResponseEntity.status(HttpStatus.OK).body("Segment deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    @Operation(summary = "Переименовать сегмент", description = "Изменяет название сегмента по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сегмент успешно переименован"),
            @ApiResponse(responseCode = "400", description = "Сегмент не найден или новое имя некорректно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/renameSegment/{id}")
    public ResponseEntity<String> renameSegment(@PathVariable Long id, @RequestBody String newName) {
        try {
            segmentService.renameSegment(id, newName);
            return ResponseEntity.status(HttpStatus.OK).body("Segment renamed successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    @Operation(summary = "Получить все сегменты", description = "Возвращает список всех сегментов в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список сегментов успешно получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/findAllSegments")
    public ResponseEntity<List<Segment>> findAllSegments() {
        try {
            List<Segment> segments = segmentService.getAllSegments();
            return ResponseEntity.ok(segments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
