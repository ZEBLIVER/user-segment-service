package com.example.user_segment_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="segments")
@Data
@Schema(description = "Информация о сегменте")
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор")
    private Long id;

    @NotBlank(message = "Имя сегмента не может быть пустым")
    @Schema(description = "Имя сегмента")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "segments")
    private List<User> users = new ArrayList<>();


}
