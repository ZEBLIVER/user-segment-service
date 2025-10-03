package com.study.segment_service.conntroller;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.User;
import com.study.segment_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}/segments")
    public ResponseEntity<Set<String>> getAllSegmentsByUserID(@PathVariable Long userId) {
        try {
            Set<String> segments = userService.getAllSegmentsByUserID(userId);
            return ResponseEntity.ok(segments);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/distribute/{segmentName}")
    public ResponseEntity<Void> distributeSegment(@PathVariable String segmentName,
                                                  @RequestBody Double percent) {
        try {
            userService.distributeSegment(segmentName,percent);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
