package com.study.segment_service.conntroller;

import com.study.segment_service.model.Segment;
import com.study.segment_service.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;

@RestController
@RequestMapping("/api/segments")
public class SegmentController {
    private final SegmentService segmentService;

    @Autowired
    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Segment> createSegment(@RequestBody Segment segment) {
        Segment savedSegment = segmentService.createSegment(segment);
        return new ResponseEntity<>(savedSegment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Segment> getSegmentById(@PathVariable Long id){
        Segment segment = segmentService.findSegmentById(id);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }

}
