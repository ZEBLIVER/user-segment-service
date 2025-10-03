package com.study.segment_service.service;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.Segment;
import com.study.segment_service.model.User;
import com.study.segment_service.repository.SegmentRepository;
import com.study.segment_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SegmentService {
    private final SegmentRepository segmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public SegmentService(SegmentRepository segmentRepository, UserRepository userRepository) {
        this.segmentRepository = segmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Segment createSegment(Segment segment) {
        String segmentName = segment.getName();
        if (segmentRepository.existsByName(segmentName)) {
            throw new IllegalArgumentException
                    ("Сегмент с именем '" + segmentName + "' уже существует.");
        }
        return segmentRepository.save(segment);
    }

    @Transactional
    public void deleteSegment(String segmentName) {
        long deletedCounter = segmentRepository.deleteByName(segmentName);

        if (deletedCounter == 0) {
            throw new ResourceNotFoundException(
                    "Сегмент с именем: '" + segmentName + "' не существует."
            );
        }
    }

    public Segment findSegmentById(Long id) {
        return segmentRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Сегмент с id '" + id + "' не существует."
                ));
    }

    @Transactional
    public Segment renameSegment(String currentSegmentName, String newSegmentName) {

        Segment segment = segmentRepository.findByName(currentSegmentName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Сегмент с именем '" + currentSegmentName + "' не существует."
                ));

        if (segmentRepository.existsByName(newSegmentName)) {
            throw new IllegalArgumentException(
                    "Сегмент с именем '" + newSegmentName + "' уже существует!"
            );
        }
        segment.setName(newSegmentName);
        return segmentRepository.save(segment);

    }

    @Transactional
    public void addUserToSegment(String segmentName, Long userId) {
        Segment segment = segmentRepository.findByName(segmentName).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Сегмент с именем '" + segmentName + "' не существует."
                ));
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь с ID '" + userId + "' не найден."
                ));
        user.getSegments().add(segment);
        segment.getUsers().add(user);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserFromSegment(String segmentName, Long userId){
        Segment segment = segmentRepository.findByName(segmentName).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Сегмент с именем '" + segmentName + "' не существует."
                ));
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь с ID '" + userId + "' не найден."
                ));
        user.getSegments().remove(segment);
        segment.getUsers().remove(user);
        userRepository.save(user);
    }
}
