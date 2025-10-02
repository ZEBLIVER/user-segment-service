package com.study.segment_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "segments")
@Data
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long segment_id;

    @Column(name = "segment_name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "segments")
    private Set<User> users = new HashSet<>();
}
