package com.study.segment_service.service;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.User;
import com.study.segment_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
