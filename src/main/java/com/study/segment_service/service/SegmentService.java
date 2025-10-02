package com.study.segment_service.service;

import com.study.segment_service.model.Segment;
import com.study.segment_service.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SegmentService {
    private final SegmentRepository segmentRepository;

    @Autowired
    public SegmentService(SegmentRepository segmentRepository){
        this.segmentRepository = segmentRepository;
    }

    public Segment createSegment(Segment segment){
        String segmentName = segment.getName();
        if (segmentRepository.existsByName(segmentName)) {
            throw new IllegalArgumentException
                    ("Сегмент с именем '" + segmentName + "' уже существует.");
        }
        return segmentRepository.save(segment);
    }

    public Segment findSegmentById(Long id){
        Optional<Segment> segmentOptional = segmentRepository.findById(id);

        if (segmentOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "Сегмент с id '" + id + "' не существует."
            );
        }
        return segmentOptional.get();
    }
}
