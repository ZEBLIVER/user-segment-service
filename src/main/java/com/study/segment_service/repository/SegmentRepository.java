package com.study.segment_service.repository;

import com.study.segment_service.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment,Long> {
    Optional<Segment> findByName(String name);
    boolean existsByName(String string);

    long deleteByName(String name);
}
