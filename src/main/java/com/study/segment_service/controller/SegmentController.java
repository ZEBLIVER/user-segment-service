package com.study.segment_service.controller;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.Segment;
import com.study.segment_service.service.SegmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Segment Management",
        description = "Операции CRUD для сегментов и управление распределением пользователей")
@RestController
@RequestMapping("/api/segments")
public class SegmentController {
    private final SegmentService segmentService;

    @Autowired
    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @Operation(summary = "Создать новый сегмент", description = "Создает сегмент с уникальным именем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сегмент успешно создан"),
            @ApiResponse(responseCode = "409", description = "Сегмент с таким именем уже существует (Конфликт)")
    })
    @PostMapping()
    public ResponseEntity<Segment> createSegment(@RequestBody Segment segment) {
        try {
            Segment savedSegment = segmentService.createSegment(segment);
            return new ResponseEntity<>(savedSegment, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Удалить сегмент", description = "Удаляет сегмент по его имени, открепляя его от всех пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сегмент успешно удален (Нет содержимого)"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден")
    })
    @DeleteMapping("/{segmentName}")
    public ResponseEntity<Void> deleteSegment(@PathVariable String segmentName) {
        try {
            segmentService.deleteSegment(segmentName);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить сегмент по ID", description = "Возвращает информацию о сегменте по его уникальному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сегмент успешно найден и возвращен"),
            @ApiResponse(responseCode = "404", description = "Сегмент с указанным ID не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Segment> getSegmentById(@PathVariable Long id) {
        try {
            Segment segment = segmentService.findSegmentById(id);
            return new ResponseEntity<>(segment, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Переименовать сегмент", description = "Переименовывает существующий сегмент.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сегмент успешно переименован (Нет содержимого)"),
            @ApiResponse(responseCode = "404", description = "Исходный сегмент не найден"),
            @ApiResponse(responseCode = "409", description = "Сегмент с новым именем уже существует (Конфликт)")
    })
    @PutMapping("/rename/{segmentName}")
    public ResponseEntity<Void> renameSegment(@PathVariable String segmentName,
                                              @RequestBody String newSegmentName) {
        try {
            segmentService.renameSegment(segmentName, newSegmentName);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Добавить пользователя в сегмент", description = "Прикрепляет пользователя к указанному сегменту.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно добавлен (Нет содержимого)"),
            @ApiResponse(responseCode = "404", description = "Сегмент или Пользователь не найден")
    })
    @PutMapping("/{segmentName}/user/{userId}")
    public ResponseEntity<Void> addUserToSegment(@PathVariable String segmentName,
                                                 @PathVariable Long userId) {
        try {
            segmentService.addUserToSegment(segmentName, userId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить пользователя из сегмента", description = "Открепляет пользователя от указанного сегмента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален из сегмента (Нет содержимого)"),
            @ApiResponse(responseCode = "404", description = "Сегмент или Пользователь не найден")
    })
    @DeleteMapping("/{segmentName}/user/{userId}")
    public ResponseEntity<Void> deleteUserFromSegment(@PathVariable String segmentName,
                                                      @PathVariable Long userId) {
        try {
            segmentService.deleteUserFromSegment(segmentName, userId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @Operation(summary = "Распределить пользователей по сегменту", description = "Распределяет указанный процент всех существующих пользователей в данный сегмент.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Распределение завершено (Нет содержимого)"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден"),
            @ApiResponse(responseCode = "400", description = "Неверное значение процента (должно быть в диапазоне (0, 100])")
    })
    @PostMapping("/distribute/{segmentName}")
    public ResponseEntity<Void> distributeUsersToSegment(
            @PathVariable String segmentName,
            @RequestBody Double percent) {
        try {
            segmentService.distributeUsersToSegment(segmentName, percent);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
