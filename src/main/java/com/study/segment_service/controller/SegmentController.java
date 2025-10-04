package com.study.segment_service.controller;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.Segment;
import com.study.segment_service.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/segments")
public class SegmentController {
    private final SegmentService segmentService;

    @Autowired
    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @PostMapping()
    public ResponseEntity<Segment> createSegment(@RequestBody Segment segment) {
        try {
            Segment savedSegment = segmentService.createSegment(segment);
            return new ResponseEntity<>(savedSegment, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{segmentName}")
    public ResponseEntity<Void> deleteSegment(@PathVariable String segmentName) {
        try {
            segmentService.deleteSegment(segmentName);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Segment> getSegmentById(@PathVariable Long id) {
        try {
            Segment segment = segmentService.findSegmentById(id);
            return new ResponseEntity<>(segment, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

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

    @PostMapping("/distribute/{segmentName}")
    public ResponseEntity<VerifyError> distributeUsersToSegment(
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
