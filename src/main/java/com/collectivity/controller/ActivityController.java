package com.collectivity.controller;

import com.collectivity.entity.ActivityEntity;
import com.collectivity.entity.AttendanceEntity;
import com.collectivity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

   
     
    @GetMapping("/{id}/activities")
    public List<ActivityEntity> getActivities(@PathVariable String id) {
        return activityService.getActivitiesByCollectivity(id);
    }

    @PostMapping("/{id}/activities")
    public List<ActivityEntity> addActivities(
            @PathVariable String id, 
            @RequestBody List<ActivityEntity> activities) {
        return activityService.createActivities(id, activities);
    }

   

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/activities/{activityId}/attendance")
    public void recordAttendance(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody List<AttendanceEntity> attendances) {
        activityService.recordAttendance(activityId, attendances);
    }

    
    @GetMapping("/{id}/activities/{activityId}/attendance")
    public List<AttendanceEntity> getAttendances(
            @PathVariable String id,
            @PathVariable String activityId) {
        return activityService.getAttendances(activityId);
    }
}