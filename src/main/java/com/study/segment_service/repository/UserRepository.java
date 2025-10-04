package com.study.segment_service.repository;

import com.study.segment_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.user_id FROM User u")
    List<Long> findAllUsersIds();

    @Modifying
    @Query(value = "INSERT INTO user_segment (user_id, segment_id) " +
            "SELECT user_id, :segmentId FROM users WHERE user_id IN :userIds " +
            "ON CONFLICT (user_id, segment_id) DO NOTHING", // Это для PostgreSQL
            nativeQuery = true)
    void addUsersToSegment(@Param("segmentId") Long segmentId, @Param("userIds") List<Long> userIds);
}
