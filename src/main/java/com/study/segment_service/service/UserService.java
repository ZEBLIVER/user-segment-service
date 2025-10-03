package com.study.segment_service.service;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.User;
import com.study.segment_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SegmentService segmentService;

    @Autowired
    public UserService(UserRepository userRepository, SegmentService segmentService) {
        this.userRepository = userRepository;
        this.segmentService = segmentService;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Set<String> getAllSegmentsByUserID(Long user_id) {
        User user = userRepository.findById(user_id).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь с ID '" + user_id + "' не существует."
                ));

        return user.getSegments().stream()
                .map(segment -> segment.getName())
                .collect(Collectors.toSet());
    }

    @Transactional
    public void distributeSegment(String segmentName, double percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException(
                    "Недопустимое значение percent"
            );
        }

        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()) {
            return;
        }

        int requestCount = (int) Math.round(allUsers.size() * percent / 100.0);

        Collections.shuffle(allUsers);

        List<User> selectedUsers = allUsers.subList(0, requestCount);

        for (User user : selectedUsers) {
            segmentService.addUserToSegment(segmentName, user.getUser_id());
        }
    }


}
