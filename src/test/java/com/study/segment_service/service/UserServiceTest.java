package com.study.segment_service.service;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.Segment;
import com.study.segment_service.model.User;
import com.study.segment_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private SegmentService segmentService;
    
    @Mock
    private UserRepository userRepository;
    


    @Test
    void createUser_SaveAndReturnUser() {
        User testUser = new User();
        testUser.setUser_id(2L);

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.createUser(testUser);

        assertNotNull(savedUser);
        assertEquals(2L,savedUser.getUser_id());

        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void findAllUsers_shouldReturnListOfUsers() {
        User user1 = new User();
        user1.setUser_id(1L);
        User user2 = new User();
        user2.setUser_id(2L);
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(List.of(user1,user2));

        List<User> actualUsers = userService.findAllUsers();

        assertEquals(2,actualUsers.size());
        assertEquals(expectedUsers, actualUsers);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllSegmentsByUserID_whenUserExists_shouldReturnSegments() {
        Long userId = 1L;
        User user = new User();
        user.setUser_id(userId);

        Segment segment1 = new Segment();
        segment1.setSegment_id(1L);
        segment1.setName("gpt1");

        Segment segment2 = new Segment();
        segment2.setSegment_id(2L);
        segment2.setName("gpt2");

        user.setSegments(new HashSet<>(Arrays.asList(segment1,segment2)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Set<String> segments = userService.getAllSegmentsByUserID(userId);

        assertNotNull(segments);
        assertEquals(2,segments.size());
        assertTrue(segments.contains("gpt1"));
        assertTrue(segments.contains("gpt2"));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllSegmentsByUserID_whenUserNotExists_shouldThrowException() {
        Long nonExistentUserId = 999L;

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getAllSegmentsByUserID(nonExistentUserId));

        verify(userRepository, times(1)).findById(nonExistentUserId);
    }

}
