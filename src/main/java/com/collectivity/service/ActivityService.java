package com.collectivity.service;

import com.collectivity.datasource.ActivityRepository;
import com.collectivity.datasource.AttendanceRepository;
import com.collectivity.entity.ActivityEntity;
import com.collectivity.entity.AttendanceEntity;
import com.collectivity.enums.AttendanceStatus;
import com.collectivity.exception.BadRequestException;
import com.collectivity.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public List<ActivityEntity> createActivities(String collectivityId, List<ActivityEntity> activities) {
        activities.forEach(activity -> {
            activity.setId(UUID.randomUUID().toString());
            activity.setCollectivityId(collectivityId);
           
            if (activity.getExecutiveDate() == null) {
                
            }
        });
        return activityRepository.saveAll(activities);
    }

 
    @Transactional
    public void recordAttendance(String activityId, List<AttendanceEntity> attendances) {
        for (AttendanceEntity incoming : attendances) {
          
            attendanceRepository.findByActivityIdAndMemberId(activityId, incoming.getMemberId())
                .ifPresentOrElse(
                    existing -> {
                       
                        if (existing.getAttendanceStatus() != AttendanceStatus.UNDEFINED) {
                            throw new BadRequestException("Status already set and cannot be changed for member: " + incoming.getMemberId());
                        }
                        
                        existing.setAttendanceStatus(incoming.getAttendanceStatus());
                        attendanceRepository.save(existing);
                    },
                    () -> {
                       
                        incoming.setId(UUID.randomUUID().toString());
                        incoming.setActivityId(activityId);
                        attendanceRepository.save(incoming);
                    }
                );
        }
    }

    public List<ActivityEntity> getActivitiesByCollectivity(String collectivityId) {
        return activityRepository.findByCollectivityId(collectivityId);
    }

    public List<AttendanceEntity> getAttendances(String activityId) {
        return attendanceRepository.findByActivityId(activityId);
    }
}