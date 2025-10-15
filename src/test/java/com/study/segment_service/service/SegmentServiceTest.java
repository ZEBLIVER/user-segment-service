package com.study.segment_service.service;

import com.study.segment_service.exeption.ResourceNotFoundException;
import com.study.segment_service.model.Segment;
import com.study.segment_service.model.User;
import com.study.segment_service.repository.SegmentRepository;
import com.study.segment_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SegmentServiceTest {

    @InjectMocks
    private SegmentService segmentService;

    @Mock
    private SegmentRepository segmentRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldSaveSegment_whenSegmentNameIsUnique() {
        Segment segment = new Segment();
        segment.setSegment_id(1L);
        segment.setName("gpt1");

        when(segmentRepository.existsByName("gpt1")).thenReturn(false);
        when(segmentRepository.save(any(Segment.class))).thenReturn(segment);

        Segment savedSegment = segmentService.createSegment(segment);

        assertNotNull(savedSegment);
        assertEquals(1L, savedSegment.getSegment_id());
        assertEquals("gpt1", savedSegment.getName());

        verify(segmentRepository, times(1)).existsByName(segment.getName());
        verify(segmentRepository, times(1)).save(segment);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenSegmentNameAlreadyExists() {
        String badSegmentName = "gpt999";
        Segment segment = new Segment();
        segment.setName(badSegmentName);
        when(segmentRepository.existsByName(badSegmentName)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> segmentService.createSegment(segment));

        verify(segmentRepository, times(1)).existsByName(badSegmentName);
        verify(segmentRepository, never()).save(any(Segment.class));
    }

    @Test
    void shouldDeleteSegment_whenSegmentIsFound() {
        String name = "gpt1";

        when(segmentRepository.deleteByName(name)).thenReturn(1L);

        segmentService.deleteSegment(name);

        verify(segmentRepository, times(1)).deleteByName(name);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenSegmentToDeleteNotFound() {
        String name = "gpt1";

        when(segmentRepository.deleteByName(name)).thenReturn(0L);

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.deleteSegment(name));

        verify(segmentRepository, times(1)).deleteByName(name);
    }

    @Test
    void shouldFindSegmentById_whenSegmentIsExist() {
        Long id = 1L;
        Segment segment = new Segment();
        segment.setName("gpt1");
        segment.setSegment_id(id);

        when(segmentRepository.findById(id)).thenReturn(Optional.of(segment));

        Segment foundSegment = segmentService.findSegmentById(id);

        assertEquals(id, foundSegment.getSegment_id());
        assertEquals("gpt1", foundSegment.getName());

        verify(segmentRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenSegmentNotExist() {
        Long nonExistentSegmentId = 999L;

        when(segmentRepository.findById(nonExistentSegmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.findSegmentById(nonExistentSegmentId));

        verify(segmentRepository, times(1)).findById(nonExistentSegmentId);
    }

    @Test
    void shouldRenameSegment_whenCurrentNameExistsAndNewNameIsUnique() {
        String oldName = "gpt1";
        String newName = "gpt2";

        Segment segmentToRename = new Segment();
        segmentToRename.setSegment_id(1L);
        segmentToRename.setName(oldName);

        when(segmentRepository.existsByName(newName)).thenReturn(false);
        when(segmentRepository.findByName(oldName)).thenReturn(Optional.of(segmentToRename));
        when(segmentRepository.save(any(Segment.class))).thenReturn(segmentToRename);

        Segment renamedSegment = segmentService.renameSegment(oldName, newName);

        assertNotNull(renamedSegment);
        assertEquals(newName, renamedSegment.getName());
        assertEquals(1L, renamedSegment.getSegment_id());

        verify(segmentRepository, times(1)).findByName(oldName);
        verify(segmentRepository, times(1)).existsByName(newName);
        verify(segmentRepository, times(1)).save(segmentToRename);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCurrentNameNotFound() {
        String oldName = "gpt1";
        String newName = "gpt2";

        when(segmentRepository.findByName(oldName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.renameSegment(oldName,newName));

        verify(segmentRepository,times(1)).findByName(oldName);
        verify(segmentRepository, never()).existsByName(anyString());
        verify(segmentRepository, never()).save(any(Segment.class));
    }

    @Test
    void shouldThrowIllegalArgumentException_whenNewNameAlreadyExists() {
        String oldName = "gpt1";
        String newName = "gpt2";
        Segment segment = new Segment();
        when(segmentRepository.findByName(oldName)).thenReturn(Optional.of(segment));
        when(segmentRepository.existsByName(newName)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> segmentService.renameSegment(oldName,newName));

        verify(segmentRepository,times(1)).existsByName(newName);
        verify(segmentRepository, never()).save(any(Segment.class));
    }


    @Test
    void shouldAddUserToSegment_whenCurrentSegmentExistsAndUserFound() {
        String segmentName = "gpt1";
        Long userId = 2L;

        Segment segment = new Segment();
        segment.setSegment_id(1L);
        segment.setName(segmentName);
        segment.setUsers(new HashSet<>());

        User user = new User();
        user.setUser_id(userId);
        user.setSegments(new HashSet<>());

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        segmentService.addUserToSegment(segmentName,userId);

        assertTrue(user.getSegments().contains(segment));
        assertTrue(segment.getUsers().contains(user));

        verify(segmentRepository, times(1)).findByName(segmentName);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenAddingUserAndSegmentNotFound() {
        String segmentName = "gpt1";
        Long userId = 1L;

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.addUserToSegment(segmentName,userId));

        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository, never()).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenAddingUserAndUserNotFound() {
        String segmentName = "gpt1";
        Long userId = 1L;
        Segment segment = new Segment();

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.addUserToSegment(segmentName,userId));

        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRemoveUserFromSegment_whenSegmentAndUserAreFound() {
        String segmentName = "gpt1";
        Long userId = 1L;

        Segment segment = new Segment();
        segment.setName(segmentName);
        segment.setUsers(new HashSet<>());

        User user = new User();
        user.setUser_id(userId);
        user.setSegments(new HashSet<>());

        user.getSegments().add(segment);
        segment.getUsers().add(user);

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        segmentService.deleteUserFromSegment(segmentName,userId);

        assertFalse(user.getSegments().contains(segment));
        assertFalse(segment.getUsers().contains(user));

        verify(segmentRepository, times(1)).findByName(segmentName);
        verify(userRepository,times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void shouldThrowResourceNotFoundException_whenDeletingSegmentNotFound() {
        String segmentName = "gpt1";
        Long userId = 1L;

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.deleteUserFromSegment(segmentName,userId));

        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository,never()).findById(userId);
        verify(userRepository,never()).save(any());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenDeletingUserNotFound() {
        String segmentName = "gpt1";
        Long userId = 1L;
        Segment segment = new Segment();

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.deleteUserFromSegment(segmentName,userId));

        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,never()).save(any());
    }

    @Test
    void distributeUsersToSegment_Success_UsersAddedToSegment() {
        String segmentName = "gpt1";
        Long segmentId = 1L;
        List<Long> allUserIds = new ArrayList<>(List.of(2L, 3L));

        Segment segment = new Segment();
        segment.setSegment_id(segmentId);
        segment.setName(segmentName);


        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findAllUsersIds()).thenReturn(allUserIds);

        segmentService.distributeUsersToSegment(segmentName,50.0);

        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository, times(1)).findAllUsersIds();

        verify(userRepository, times(1)).addUsersToSegment(
                eq(segmentId), // Проверяем, что передан сегмент 1L
                argThat(selectedUsers -> selectedUsers.size() == 1)
        );
    }

    @Test
    void distributeUsersToSegment_PercentIsZero_ThrowsIllegalArgumentException() {
        String segmentName = "gpt1";
        double percent = 0.0;
        assertThrows(IllegalArgumentException.class,
                () -> segmentService.distributeUsersToSegment(segmentName,percent));
        verify(segmentRepository,never()).findByName(segmentName);
        verify(userRepository,never()).findAllUsersIds();
        verify(userRepository, never()).addUsersToSegment(anyLong(), anyList());
    }

    @Test
    void distributeUsersToSegment_PercentGreaterThan100_ThrowsIllegalArgumentException() {
        String segmentName = "gpt1";
        double percent = 100.1;
        assertThrows(IllegalArgumentException.class,
                () -> segmentService.distributeUsersToSegment(segmentName,percent));
        verify(segmentRepository,never()).findByName(segmentName);
        verify(userRepository,never()).findAllUsersIds();
        verify(userRepository, never()).addUsersToSegment(anyLong(), anyList());
    }

    @Test
    void distributeUsersToSegment_SegmentNotFound_ThrowsResourceNotFoundException() {
        String segmentName = "gpt1";
        double percent = 50.0;
        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> segmentService.distributeUsersToSegment(segmentName,percent));
        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository,never()).findAllUsersIds();
        verify(userRepository, never()).addUsersToSegment(anyLong(), anyList());
    }

    @Test
    void distributeUsersToSegment_EmptyUserList_ReturnsImmediately() {
        String segmentName = "gpt1";
        double percent = 50.0;
        Segment segment = new Segment();

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findAllUsersIds()).thenReturn(Collections.emptyList());
        segmentService.distributeUsersToSegment(segmentName,percent);

        verify(segmentRepository,times(1)).findByName(segmentName);
        verify(userRepository,times(1)).findAllUsersIds();
        verify(userRepository, never()).addUsersToSegment(anyLong(), anyList());
    }

    @Test
    void distributeUsersToSegment_ResultingCountIsZero_ReturnsImmediately() {
        String segmentName = "gpt1";
        Long segmentId = 2L;

        Segment segment = new Segment();
        segment.setSegment_id(segmentId);
        segment.setName(segmentName);

        List<Long> allUserIds = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            allUserIds.add(i);
        }
        double zeroPercent = 0.001;

        when(segmentRepository.findByName(segmentName)).thenReturn(Optional.of(segment));
        when(userRepository.findAllUsersIds()).thenReturn(allUserIds);

        segmentService.distributeUsersToSegment(segmentName, zeroPercent);

        verify(segmentRepository, times(1)).findByName(segmentName);
        verify(userRepository, times(1)).findAllUsersIds();
        verify(userRepository, never()).addUsersToSegment(anyLong(), anyList());
    }

}