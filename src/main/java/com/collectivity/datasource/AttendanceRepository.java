package com.collectivity.datasource;

import com.collectivity.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {

    Optional<AttendanceEntity> findByActivityIdAndMemberId(String activityId, String memberId);
    
    List<AttendanceEntity> findByActivityId(String activityId);
}