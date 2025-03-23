package com.example.user_segment_service.service;


import com.example.user_segment_service.model.User;
import com.example.user_segment_service.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {


    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addNewUser(User user) {
        if (userRepo.findByName(user.getName()).isPresent()) {
            throw new IllegalStateException("Пользователь с таким именем уже существует!");
        }
        User savedUser = userRepo.save(user);
        log.info("User {} added successfully", savedUser.getId());
    }

    @Transactional
    public void deleteUser(Long id) {

        User user = userRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким ID не существует"));

        user.getSegments().forEach(segment ->
                segment.getUsers().remove(user));

        user.getSegments().clear();

        userRepo.delete(user);

        log.info("Пользователь {} успешно удален", user.getName());
    }


    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
