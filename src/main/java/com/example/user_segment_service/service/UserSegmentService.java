package com.example.user_segment_service.service;

import com.example.user_segment_service.model.Segment;
import com.example.user_segment_service.model.User;
import com.example.user_segment_service.repository.SegmentRepo;
import com.example.user_segment_service.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserSegmentService {

    private final UserRepo userRepo;
    private final SegmentRepo segmentRepo;

    @Autowired
    public UserSegmentService(UserRepo userRepo, SegmentRepo segmentRepo) {
        this.userRepo = userRepo;
        this.segmentRepo = segmentRepo;
    }

    public void addUserToSegment(Long userId, String segmentName) {
        // Находим пользователя
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Находим сегмент по имени
        Segment segment = segmentRepo.findByName(segmentName)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found with name: " + segmentName));

        if (user.getSegments().contains(segment)) {
            throw new IllegalStateException("User is already in the segment");
        }

        // Добавляем пользователя в сегмент
        user.getSegments().add(segment);
        segment.getUsers().add(user);

        // Сохраняем изменения
        userRepo.save(user);
        segmentRepo.save(segment);

        log.info("User {} added to segment {}", userId, segmentName);
    }

    public void removeUserFromSegment(Long userId, String segmentName) {
        // Находим пользователя
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Находим сегмент по имени
        Segment segment = segmentRepo.findByName(segmentName)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found with name: " + segmentName));

        user.getSegments().remove(segment);
        segment.getUsers().remove(user);

        userRepo.save(user);
        segmentRepo.save(segment);

        log.info("User {} removed from segment {}", userId, segmentName);
    }

    public void distributeSegmentToPercentage(String segmentName, double percentage) {
        // Проверка корректности процента
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        // Проверка существования сегмента
        Segment segment = segmentRepo.findByName(segmentName)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found with name: " + segmentName));

        // Получаем всех пользователей
        List<User> allUsers = userRepo.findAll();

        // Фильтрация - оставляем только пользователей, которые ещё НЕ в этом сегменте
        List<User> usersNotInSegment = allUsers.stream()
                .filter(user -> !user.getSegments().contains(segment))
                .collect(Collectors.toList());

        // Перемешиваем отфильтрованных пользователей для случайного распределения
        Collections.shuffle(usersNotInSegment);

        // Рассчитываем количество пользователей для добавления
        // Важно: теперь считаем от общего числа пользователей, а не от отфильтрованных
        int totalUsersToAdd = (int) Math.round(allUsers.size() * (percentage / 100.0));

        // Считаем, сколько пользователей уже в сегменте
        int usersAlreadyInSegment = allUsers.size() - usersNotInSegment.size();

        // Сколько нужно добавить дополнительно
        int usersToAdd = Math.max(0, totalUsersToAdd - usersAlreadyInSegment);

        // Добавляем сегмент выбранным пользователям
        List<User> usersToUpdate = new ArrayList<>();
        for (int i = 0; i < Math.min(usersToAdd, usersNotInSegment.size()); i++) {
            User user = usersNotInSegment.get(i);
            user.getSegments().add(segment);
            usersToUpdate.add(user);
        }

        // Пакетное сохранение
        if (!usersToUpdate.isEmpty()) {
            userRepo.saveAll(usersToUpdate);
        }

        log.info("Segment {} distributed to {}% of users ({} users already in segment, {} users added)",
                segmentName, percentage, usersAlreadyInSegment, usersToUpdate.size());
    }

    public List<String> getUserSegments(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return user.getSegments().stream()
                .map(Segment::getName)
                .collect(Collectors.toList());
    }

}
