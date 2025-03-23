package com.example.user_segment_service.repository;

import com.example.user_segment_service.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SegmentRepo extends JpaRepository<Segment, Long>{

    Optional<Segment> findByName(String name);
}
