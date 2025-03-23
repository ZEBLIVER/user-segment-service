package com.example.user_segment_service.service;

import com.example.user_segment_service.model.Segment;
import com.example.user_segment_service.repository.SegmentRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SegmentService {

    private final SegmentRepo segmentRepo;

    @Autowired
    public SegmentService(SegmentRepo segmentRepo) {
        this.segmentRepo = segmentRepo;
    }

    public void addNewSegment(Segment segment) {
        if (segmentRepo.findByName(segment.getName()).isPresent()) {
            log.warn("Попытка добавить уже существующий сегмент: {}", segment.getName());
            throw new IllegalStateException("Сегмент с таким именем уже существует");
        }
        segmentRepo.save(segment);
        log.info("Сегмент {} успешно добавлен", segment.getName());
    }

    @Transactional
    public void deleteSegment(Long segmentId) {
        Segment segment = segmentRepo.findById(segmentId)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found with id: " + segmentId));

        // Очистка связей перед удалением
        segment.getUsers().forEach(user -> user.getSegments().remove(segment));
        segment.getUsers().clear();

        segmentRepo.delete(segment);
        log.info("Сегмент {} успешно удален", segment.getName());
    }

    @Transactional
    public void renameSegment(Long id, String newName) {
        Optional<Segment> segmentOpt = segmentRepo.findById(id);

        if(!segmentOpt.isPresent()) {
            throw new IllegalStateException("Сегмент с таким ID не существует");
        }

        Segment segment = segmentOpt.get();
        segment.setName(newName);  // Меняем имя
        segmentRepo.save(segment); // Сохраняем обновлённый сегмент

        log.info("Сегмент {} переименован в {}", segmentOpt.get().getName(), newName);
    }

    public List<Segment> getAllSegments() {
        return segmentRepo.findAll();
    }
}
