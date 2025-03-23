package com.example.user_segment_service.repository;

import com.example.user_segment_service.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<Object> findByName(@NotBlank(message = "Имя не может быть пустым") @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов") String name);
}
